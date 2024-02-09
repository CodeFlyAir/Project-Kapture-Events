package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.entity.Society;
import com.kaptureevents.KaptureEvents.model.EventModel;
import com.kaptureevents.KaptureEvents.repository.EventRepository;
import com.kaptureevents.KaptureEvents.repository.SocietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService{

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SocietyRepository societyRepository;

    //saving event to DB
    @Override
    public ResponseEntity<Events> registerEvents(EventModel eventModel, String emailId) {
        Society societyId;
        Optional<Society> societyOptional = societyRepository.findByEmailId(emailId);

        if(societyOptional.isPresent())
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
        //events.setId(eventModel.g);
        events.setSponsors(eventModel.getSponsors());
        events.setSpecialGuest(eventModel.getSpecialGuest());
        events.setSubEvent(eventModel.getSubEvent());
        events.setUpdates(eventModel.getUpdates());
        events.setEventStatus(eventModel.getEventStatus());

        return ResponseEntity.ok(eventRepository.save(events));
    }

    //get event from DB
    @Override
    public ResponseEntity<Events> eventProfile(String name) {

        Optional<Events> eventsOptional = eventRepository.findByName(name);
        if(eventsOptional.isPresent()){
            Events events = eventsOptional.get();
            return new ResponseEntity<>(events,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}