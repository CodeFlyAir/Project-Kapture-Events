package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.entity.Society;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        return ResponseEntity.ok(eventRepository.save(events));
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

}
