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
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    UserModel userModel = UserModel.getInstance();

    @GetMapping("/events")
    public String getEvents() {
        return userModel.getName();
    }


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

    //Add Contact Details to an event
    @PostMapping("/{eventName}/addContact")
    public ResponseEntity<Events> addEventContact(
            @RequestPart(value = "imageFile") MultipartFile imageFile,
            @RequestPart(value = "jsonData") EventContactModel eventContact,
            @PathVariable String eventName) {
        try {
            return eventService.addEventContact(eventContact, eventName, imageFile);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{eventName}/deleteContact/{contact}")
    public ResponseEntity<Events> deleteEventContact(
            @PathVariable("eventName") String eventName,
            @PathVariable("contact") Long contact) {
        try {
            return eventService.deleteEventContact(eventName, contact);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }


    //Get event from DB
    @GetMapping("/profile/{name}")
    private ResponseEntity<Events> eventProfile(@PathVariable String name) {
        return eventService.eventProfile(name);
    }

    //Delete from DB
    @DeleteMapping("/profile/delete/{name}")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable String name){
        return eventService.deleteEvent(name);
    }
}