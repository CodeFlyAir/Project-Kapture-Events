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
            EventContactModel eventContact, UUID eventName, MultipartFile file);

    ResponseEntity<Events> eventProfile(UUID eventId);

    ResponseEntity<List<Student>> findAllStudentsRegisteredForEvent(String eventId);

    ResponseEntity<Events> deleteEventContact(UUID eventName, Long contact);

    ResponseEntity<Boolean> deleteEvent(UUID name);

    ResponseEntity<String> editTeamFormationGuidelines(UUID name, String guidelines);

    ResponseEntity<String> editRewards(UUID eventName, String rewards);

    ResponseEntity<String> editEligibilityCriteria(UUID eventName, String eligibilityCriteria);

    ResponseEntity<Events> addResource(UUID eventName, MultipartFile file);

    ResponseEntity<Events> deleteResource(UUID eventName, String fileName);

    ResponseEntity<Events> addSponsor(UUID eventId, MultipartFile file);

    ResponseEntity<Events> deleteSponsor(UUID eventId, String fileName);

    ResponseEntity<Events> addSpecialGuest(UUID eventId, SpecialGuestModel specialGuestModel, MultipartFile image);

    ResponseEntity<Events> deleteSpecialGuest(UUID eventId, SpecialGuestModel specialGuestModel);

    ResponseEntity<List<Events>> getEvents();

    ResponseEntity<List<EventPreviewModel>> getEventsPreview();

    ResponseEntity<List<EventPreviewModel>> getEventsForHomeWithFilter(String filters);

    ResponseEntity<Events> addNewSubEvent(UUID eventName, SubEventsModel subEventsModel);

    ResponseEntity<Events> deleteSubEvent(UUID eventName, SubEventsModel subEventsModel);

    ResponseEntity<Events> addUpdate(UUID eventName, UpdateModel updateModel);


    ResponseEntity<Events> addSocialMediaLinks(UUID eventName, SocialMediaLinksModel socialMediaLinksModel);
}
