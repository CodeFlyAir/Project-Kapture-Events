package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.entity.Student;
import com.kaptureevents.KaptureEvents.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface EventService {
    ResponseEntity<Events> registerEvents
            (EventModel eventModel, MultipartFile thumbnail, String emailId);

    ResponseEntity<Events> addEventContact(
            EventContactModel eventContact, String eventName, MultipartFile file);

    ResponseEntity<Events> eventProfile(UUID eventId);

    ResponseEntity<List<Student>> findAllStudentsRegisteredForEvent(String eventId);

    ResponseEntity<Events> deleteEventContact(String eventName, Long contact);

    ResponseEntity<Boolean> deleteEvent(UUID name);

    ResponseEntity<String> editTeamFormationGuidelines(String name, String guidelines);

    ResponseEntity<String> editRewards(String eventName, String rewards);

    ResponseEntity<String> editEligibilityCriteria(String eventName, String eligibilityCriteria);

    ResponseEntity<Events> addResource(String eventName, MultipartFile file);

    ResponseEntity<Events> deleteResource(String eventName, String fileName);

    ResponseEntity<Events> addSponsor(UUID eventId, MultipartFile file);

    ResponseEntity<Events> deleteSponsor(UUID eventId, String fileName);

    ResponseEntity<Events> addSpecialGuest(UUID eventId, SpecialGuestModel specialGuestModel, MultipartFile image);

    ResponseEntity<Events> deleteSpecialGuest(UUID eventId, SpecialGuestModel specialGuestModel);

    ResponseEntity<List<Events>> getEvents();

    ResponseEntity<List<EventPreviewModel>> getEventsPreview();

    ResponseEntity<List<EventPreviewModel>> getEventsForHomeWithFilter(String filters);

    ResponseEntity<Events> addNewSubEvent(String eventName, SubEventsModel subEventsModel);

    ResponseEntity<Events> deleteSubEvent(String eventName, SubEventsModel subEventsModel);

    ResponseEntity<Events> addUpdate(String eventName, UpdateModel updateModel);


    ResponseEntity<Events> addSocialMediaLinks(String eventName, SocialMediaLinksModel socialMediaLinksModel);
}
