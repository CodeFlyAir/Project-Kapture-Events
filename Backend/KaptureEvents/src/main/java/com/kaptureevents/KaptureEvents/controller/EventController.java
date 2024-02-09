package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.EventContactModel;
import com.kaptureevents.KaptureEvents.model.EventModel;
import com.kaptureevents.KaptureEvents.model.UserModel;
import com.kaptureevents.KaptureEvents.service.EventService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/events")
public class EventController {

    UserModel userModel = UserModel.getInstance();

    @GetMapping("/events")
    public String getEvents() {
        return userModel.getName();

    }

    @Autowired
    private EventService eventService;

    //Event registration
    @PostMapping("/register/{emailId}")
    public ResponseEntity<Events> registerEvents(@Valid @RequestBody EventModel eventModel, @PathVariable String emailId) {
        try {
            return eventService.registerEvents(eventModel, emailId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{eventName}/addContact")
    public ResponseEntity<EventContactModel> addEventContact(
            @Valid @RequestBody EventContactModel eventContact,
            @PathVariable String eventName) {
        try {
            return eventService.addEventContact(eventContact,eventName);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    //Get event from DB
    @GetMapping("/profile/{name}")
    private ResponseEntity<Events> eventProfile(@PathVariable String name){
        return eventService.eventProfile(name);
    }
}