package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.dto.FileDto;
import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.entity.Society;
import com.kaptureevents.KaptureEvents.model.*;
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

import java.sql.Date;
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

    @Override
    public ResponseEntity<List<Events>> getEvents() {
        try {
            return ResponseEntity.ok(eventRepository.findAll());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Events>> getEventsWithFilter(String filters) {
        try {
            Date date = new Date(System.currentTimeMillis());

            if ("today".equalsIgnoreCase(filters)) {
                return eventRepository.findByStartDateEquals(date)
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.noContent().build());
                
            } else if ("tomorrow".equalsIgnoreCase(filters)) {
               java.util.Date utilDate = new java.util.Date(date.getTime());

                Calendar calendar = Calendar.getInstance(); // Current Instance
                calendar.setTime(utilDate);
                calendar.add(Calendar.DAY_OF_MONTH, 1); // Add 1 day to current date
                
                date = new java.sql.Date(calendar.getTime().getTime());
                return eventRepository.findByStartDateEquals(date)
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.noContent().build());
                
            } else if ("this-month".equalsIgnoreCase(filters)) {
                Calendar endOfMonth = Calendar.getInstance();
                endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date endDate = new Date(endOfMonth.getTimeInMillis());

                return eventRepository.findByStartDateBetween(date, endDate)
                        .map(ResponseEntity::ok).
                        orElseGet(() -> ResponseEntity.noContent().build());

            }else {
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Events> addNewSubEvent(String eventName, SubEventsModel subEventsModel) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);
        Events events;
        if (eventsOptional.isPresent()) {
            events = eventsOptional.get();
        }
        else
            return ResponseEntity.notFound().build();

        List<SubEventsModel> subEventsModelList = new ArrayList<>();
        subEventsModelList = events.getSubEvent();
         SubEventsModel subEventsModel1 = new SubEventsModel();

         subEventsModel1.setName(subEventsModel.getName());
         subEventsModel1.setDesc(subEventsModel.getDesc());
         subEventsModel1.setDate(subEventsModel.getDate());
         subEventsModel1.setTime(subEventsModel.getTime());
         subEventsModel1.setVenue(subEventsModel.getVenue());

         subEventsModelList.add(subEventsModel1);
         events.setSubEvent(subEventsModelList);
         return ResponseEntity.ok(eventRepository.save(events));
    }

    @Override
    public ResponseEntity<Events> deleteSubEvent(String eventName, SubEventsModel subEventsModel) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);
        Events events;
        if (eventsOptional.isPresent()) {
            events = eventsOptional.get();
        }
        else
            return ResponseEntity.notFound().build();

        List<SubEventsModel> subEvents = new ArrayList<>();
        subEvents =  events.getSubEvent();
        try{
            if (subEvents!=null){
                Optional<SubEventsModel> subEventsModelOptional = subEvents.stream().filter(event -> event.getName().equals(subEventsModel.
                        getName())).findFirst();
                if(subEventsModelOptional.isPresent()){
                    subEvents.remove(subEventsModelOptional.get());
                    events.setSubEvent(subEvents);
                    return ResponseEntity.ok(eventRepository.save(events));
                }
                else {
                    return ResponseEntity.notFound().build();
                }
            }else{
                return ResponseEntity.status(new ErrorResponse("No Sub Events Found",HttpStatus.NOT_FOUND).getStatus()).body(null);
            }
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Events> addUpdate(String eventName, UpdateModel updateModel) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);
        Events events;
        if (eventsOptional.isPresent()) {
            events = eventsOptional.get();
        }
        else
            return ResponseEntity.notFound().build();

        List<UpdateModel> updateModelList = new ArrayList<>();
        updateModelList = events.getUpdates();
        UpdateModel updateModel1 = new UpdateModel();

        updateModel1.setDate(updateModel.getDate());
        updateModel1.setMessage(updateModel.getMessage());

        updateModelList.add(updateModel1);
        events.setUpdates(updateModelList);
        return ResponseEntity.ok(eventRepository.save(events));
    }

    @Override
    public ResponseEntity<Events> addSocialMediaLinks(String eventName, SocialMediaLinksModel socialMediaLinksModel) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);
        Events events;
        if (eventsOptional.isPresent()) {
            events = eventsOptional.get();
        }
        else
            return ResponseEntity.notFound().build();
        SocialMediaLinksModel socialMediaLinks = new SocialMediaLinksModel();

        socialMediaLinks.setInstagram(socialMediaLinksModel.getInstagram());
        socialMediaLinks.setFacebook(socialMediaLinksModel.getFacebook());
        socialMediaLinks.setOther(socialMediaLinksModel.getOther());

        events.setSocialMedia(socialMediaLinksModel);
        return ResponseEntity.ok(eventRepository.save(events));

    }


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
        events.setSponsors(new ArrayList<>());
        events.setSpecialGuest(new ArrayList<>());
        events.setSocialMedia(eventModel.getSocialMedia());

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
            if (contactList.stream()
                    .anyMatch(contact -> contact.getContact()
                            .equals(eventContactModel.getContact()))) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            try {
                eventContactModel.setImage(dataBucketUtil.uploadFile(file));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }

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

                try {
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
                } catch (Exception e) {
                    return ResponseEntity.internalServerError().build();
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

            try {
                resources.add(dataBucketUtil.uploadFile(file));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }

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
                    try {
                        if (dataBucketUtil.deleteFile(fileName)) {
                            iterator.remove();

                            additionalDetails.setResources(resources);
                            events.setAdditionalDetails(additionalDetails);

                            eventRepository.save(events);

                            return ResponseEntity.ok(events);
                        } else {
                            return ResponseEntity.internalServerError().build();
                        }
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                }
            }

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Events> addSponsor(String eventName, MultipartFile file) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();
            List<SponsorsModel> sponsors = events.getSponsors();

            if (sponsors == null) {
                sponsors = new ArrayList<>();
            }

            SponsorsModel sponsorsModel = new SponsorsModel();

            try {
                sponsorsModel.setSponsor(dataBucketUtil.uploadFile(file));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }

            sponsors.add(sponsorsModel);

            events.setSponsors(sponsors);

            return ResponseEntity.ok(eventRepository.save(events));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Events> deleteSponsor(String eventName, String fileName) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();
            List<SponsorsModel> sponsorsModel = events.getSponsors();

            if (sponsorsModel == null) {
                return ResponseEntity.noContent().build();
            }

            Iterator<SponsorsModel> iterator = sponsorsModel.iterator();
            while (iterator.hasNext()) {
                FileDto fileDto = iterator.next().getSponsor();
                if (fileDto.getFileName().equals(fileName)) {
                    try {
                        if (dataBucketUtil.deleteFile(fileName)) {
                            iterator.remove();

                            events.setSponsors(sponsorsModel);
                            eventRepository.save(events);

                            return ResponseEntity.ok(events);
                        } else {
                            return ResponseEntity.internalServerError().build();
                        }
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                }
            }

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Events> addSpecialGuest(String eventName, SpecialGuestModel specialGuestModel, MultipartFile image) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();

            List<SpecialGuestModel> specialGuestModelList = events.getSpecialGuest();
            if (specialGuestModelList == null) {
                specialGuestModelList = new ArrayList<>();
            }

            // Check if the special guest already exists in the list
            if (specialGuestModelList.stream()
                    .anyMatch(guest -> guest.getName()
                            .equals(specialGuestModel.getName()))) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            try {
                specialGuestModel.setImage(dataBucketUtil.uploadFile(image));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
            specialGuestModelList.add(specialGuestModel);
            events.setSpecialGuest(specialGuestModelList);

            return ResponseEntity.ok(eventRepository.save(events));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Events> deleteSpecialGuest(String eventName, SpecialGuestModel specialGuestModel) {
        Optional<Events> eventsOptional = eventRepository.findByName(eventName);

        if (eventsOptional.isPresent()) {
            Events events = eventsOptional.get();
            List<SpecialGuestModel> specialGuestModelList = events.getSpecialGuest();

            if (specialGuestModelList == null) {
                return ResponseEntity.noContent().build();
            }

            Iterator<SpecialGuestModel> iterator = specialGuestModelList.iterator();
            while (iterator.hasNext()) {
                SpecialGuestModel guestModel = iterator.next();
                if (guestModel.equals(specialGuestModel)) {
                    try {
                        if (dataBucketUtil.deleteFile(guestModel.getImage().getFileName())) {
                            iterator.remove();

                            events.setSpecialGuest(specialGuestModelList);
                            eventRepository.save(events);

                            return ResponseEntity.ok(events);
                        } else {
                            return ResponseEntity.internalServerError().build();
                        }
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                    }
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
}
