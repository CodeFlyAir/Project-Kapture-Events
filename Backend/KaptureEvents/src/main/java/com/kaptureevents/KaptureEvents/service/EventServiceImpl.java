package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.dto.FileDto;
import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.entity.Society;
import com.kaptureevents.KaptureEvents.model.EventAdditionalDetailsModel;
import com.kaptureevents.KaptureEvents.model.EventContactModel;
import com.kaptureevents.KaptureEvents.model.EventModel;
import com.kaptureevents.KaptureEvents.repository.EventRepository;
import com.kaptureevents.KaptureEvents.repository.SocietyRepository;
import com.kaptureevents.KaptureEvents.utils.DataBucketUtil;
import com.kaptureevents.KaptureEvents.utils.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service

@Slf4j
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private DataBucketUtil dataBucketUtil;

    //saving event to DB
    @Override
    public ResponseEntity<Events> registerEvents(EventModel eventModel, String emailId) {
        Society societyId;
        Optional<Society> societyOptional = societyRepository.findByEmailId(emailId);

        if (societyOptional.isPresent())
            societyId = societyOptional.get();
        else
            return ResponseEntity.notFound().build();

        Events events = new Events();

        //setting the accepted event details to Events object
        events.setName(eventModel.getName());
        events.setStartDate(eventModel.getStartDate());
        events.setEndDate(eventModel.getEndDate());
        events.setContact(eventModel.getContact());
        events.setDescription(eventModel.getDescription());
        events.setAdditionalDetails(eventModel.getAdditionalDetails());
        events.setSocietyId(societyId);
        events.setSponsors(eventModel.getSponsors());
        events.setSpecialGuest(eventModel.getSpecialGuest());
        events.setSubEvent(eventModel.getSubEvent());
        events.setUpdates(eventModel.getUpdates());
        events.setEventStatus(eventModel.getEventStatus());
        events.setAdditionalDetails(new EventAdditionalDetailsModel());
        events.setContact(new ArrayList<>());

        return ResponseEntity.ok(eventRepository.save(events));
    }

    //get event from DB
    @Override
    public ResponseEntity<Events> eventProfile(String name) {
        Optional<Events> eventsOptional = eventRepository.findByName(name);
        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();
            return new ResponseEntity<>(events, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity<Events> addEventContact(
            EventContactModel eventContactModel, String eventName, MultipartFile file) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            List<EventContactModel> contactList = events.getContact();

            if (contactList == null) {
                contactList = new ArrayList<>();
            }
            // Check if the contact number already exists in the list
            boolean isDuplicate = contactList.stream()
                    .anyMatch(contact -> contact.getContact().equals(eventContactModel.getContact()));

            if (isDuplicate) {
                ErrorResponse errorResponse = new ErrorResponse("Duplicate contact number", HttpStatus.BAD_REQUEST);
                return ResponseEntity.status(errorResponse.getStatus()).body(events);
            }

            log.info("Before Upload");
            eventContactModel.setImage(dataBucketUtil.uploadFile(file));
            log.info("After Upload");

            contactList.add(eventContactModel);

            // Update the contact list in the Events object
            events.setContact(contactList);

            return ResponseEntity.ok(eventRepository.save(events));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Events> deleteEventContact(String eventName, Long contactNumber) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            // Get the list of contacts from the event
            List<EventContactModel> contactList = events.getContact();

            if (contactList != null) {
                Optional<EventContactModel> contactToRemoveOptional = contactList.stream()
                        .filter(contact -> contact.getContact().equals(contactNumber))
                        .findFirst();

                if (contactToRemoveOptional.isPresent() &&
                        dataBucketUtil.deleteFile(
                                contactToRemoveOptional
                                        .get()
                                        .getImage()
                                        .getFileName())) {
                    contactList.remove(contactToRemoveOptional.get());
                    events.setContact(contactList);

                    return ResponseEntity.ok(eventRepository.save(events));
                } else {
                    return ResponseEntity.status(
                                    new ErrorResponse("Unable to Delete", HttpStatus.NOT_FOUND)
                                            .getStatus())
                            .body(null);
                }
            }
        }
        return ResponseEntity.status(
                        new ErrorResponse("Event Not Present", HttpStatus.BAD_REQUEST)
                                .getStatus())
                .body(null);
    }

    //delete from DB
    @Override
    public ResponseEntity<Boolean> deleteEvent(String name) {
        UUID eventId;
        Optional<Events> events = eventRepository.findByName(name);
        if (events.isPresent()) {
            eventId = events.get().getEvent_id();
            eventRepository.deleteById(eventId);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> editTeamFormationGuidelines(String name, String guidelines) {
        Optional<Events> eventsOptional = eventRepository.findByName(name);
        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            EventAdditionalDetailsModel additionalDetails = events.getAdditionalDetails();

            if (additionalDetails == null)
                additionalDetails = new EventAdditionalDetailsModel();

            additionalDetails.setTeamFormationGuidelines(guidelines);
            events.setAdditionalDetails(additionalDetails);

            eventRepository.save(events);
            return ResponseEntity.ok(guidelines);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> editRewards(String eventName, String rewards) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);
        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            EventAdditionalDetailsModel additionalDetails = events.getAdditionalDetails();

            if (additionalDetails == null)
                additionalDetails = new EventAdditionalDetailsModel();

            additionalDetails.setRewards(rewards);
            events.setAdditionalDetails(additionalDetails);

            eventRepository.save(events);
            return ResponseEntity.ok(rewards);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> editEligibilityCriteria(String eventName, String eligibilityCriteria) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);
        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            EventAdditionalDetailsModel additionalDetails = events.getAdditionalDetails();

            if (additionalDetails == null)
                additionalDetails = new EventAdditionalDetailsModel();

            additionalDetails.setEligibilityCriteria(eligibilityCriteria);
            events.setAdditionalDetails(additionalDetails);

            eventRepository.save(events);
            return ResponseEntity.ok(eligibilityCriteria);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Events> addResource(String eventName, MultipartFile file) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();
            EventAdditionalDetailsModel additionalDetails = events.getAdditionalDetails();

            List<FileDto> resources = additionalDetails.getResources();

            if (resources == null) {
                resources = new ArrayList<>();
            }

            resources.add(dataBucketUtil.uploadFile(file));
            additionalDetails.setResources(resources);
            events.setAdditionalDetails(additionalDetails);

            return ResponseEntity.ok(eventRepository.save(events));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Events> deleteResource(String eventName, String fileName) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();
            EventAdditionalDetailsModel additionalDetails = events.getAdditionalDetails();

            List<FileDto> resources = additionalDetails.getResources();

            if (resources == null) {
                return ResponseEntity.internalServerError().build();
            }

            Iterator<FileDto> iterator = resources.iterator();
            while (iterator.hasNext()) {
                FileDto fileDto = iterator.next();
                if (fileDto.getFileName().equals(fileName)) {
                    if (dataBucketUtil.deleteFile(fileName)) {
                        iterator.remove();

                        additionalDetails.setResources(resources);
                        events.setAdditionalDetails(additionalDetails);

                        eventRepository.save(events);

                        return ResponseEntity.ok(events);
                    } else {
                        return ResponseEntity.internalServerError().build();
                    }
                }
            }

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();
    }

}
