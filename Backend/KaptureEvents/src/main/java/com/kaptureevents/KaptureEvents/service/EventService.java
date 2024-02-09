package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.EventContactModel;
import com.kaptureevents.KaptureEvents.model.EventModel;
import org.springframework.http.ResponseEntity;

public interface EventService {
    ResponseEntity<Events> registerEvents(EventModel eventModel, String emailId);

    ResponseEntity<EventContactModel> addEventContact(EventContactModel eventContact, String eventName);

    ResponseEntity<Events> eventProfile(String name);
}
