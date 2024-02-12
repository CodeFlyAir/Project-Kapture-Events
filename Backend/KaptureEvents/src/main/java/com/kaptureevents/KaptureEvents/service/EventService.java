package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.EventContactModel;
import com.kaptureevents.KaptureEvents.model.EventModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface EventService {
    ResponseEntity<Events> registerEvents(EventModel eventModel, String emailId);

    ResponseEntity<Events> addEventContact(
            EventContactModel eventContact, String eventName, MultipartFile file);

    ResponseEntity<Events> eventProfile(String name);

    ResponseEntity<Events> deleteEventContact(String eventName, Long contact);

    ResponseEntity<Boolean> deleteEvent(String name);
}
