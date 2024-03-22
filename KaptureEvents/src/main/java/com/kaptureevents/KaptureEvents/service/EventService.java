package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.dto.FileDto;
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

    ResponseEntity<List<EventContactModel>> addEventContact(
            EventContactModel eventContact, UUID eventName, MultipartFile file);

    ResponseEntity<Events> eventProfile(UUID eventId);

    ResponseEntity<List<Student>> findAllStudentsRegisteredForEvent(String eventId);

    ResponseEntity<List<EventContactModel>> deleteEventContact(UUID eventName, Long contact);

    ResponseEntity<Boolean> deleteEvent(UUID name);

    ResponseEntity<String> editTeamFormationGuidelines(UUID name, String guidelines);

    ResponseEntity<String> editRewards(UUID eventName, String rewards);

    ResponseEntity<String> editEligibilityCriteria(UUID eventName, String eligibilityCriteria);

    ResponseEntity<List<FileDto>> addResource(UUID eventName, MultipartFile file);

    ResponseEntity<List<FileDto>> deleteResource(UUID eventName, String fileName);

    ResponseEntity<List<SponsorsModel>> addSponsor(UUID eventId, MultipartFile file);

    ResponseEntity<List<SponsorsModel>> deleteSponsor(UUID eventId, String fileName);

    ResponseEntity<List<SpecialGuestModel>> addSpecialGuest(UUID eventId, SpecialGuestModel specialGuestModel, MultipartFile image);

    ResponseEntity<List<SpecialGuestModel>> deleteSpecialGuest(UUID eventId, String name, String fileName);

    ResponseEntity<List<Events>> getEvents();

    ResponseEntity<List<EventPreviewModel>> getEventsPreview();

    ResponseEntity<List<EventPreviewModel>> getEventsForHomeWithFilter(String filters);

    ResponseEntity<List<SubEventsModel>> addNewSubEvent(UUID eventName, SubEventsModel subEventsModel);

    ResponseEntity<List<SubEventsModel>> deleteSubEvent(UUID eventName, SubEventsModel subEventsModel);

    ResponseEntity<List<UpdateModel>> addUpdate(UUID eventName, String updateMessage);


    ResponseEntity<SocialMediaLinksModel> addSocialMediaLinks(UUID eventName, SocialMediaLinksModel socialMediaLinksModel);
}
