package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.dto.FileDto;
import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.entity.Student;
import com.kaptureevents.KaptureEvents.model.*;
import com.kaptureevents.KaptureEvents.service.EventService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/events")
@CrossOrigin(
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*"
)
public class EventController {

    @Autowired
    private EventService eventService;

    UserModel userModel = UserModel.getInstance();

    @GetMapping("")
    public ResponseEntity<List<EventPreviewModel>> getEventsForHome(@RequestParam(required = false) String filters) {
        try {
            if (filters != null) {
                return eventService.getEventsForHomeWithFilter(filters);
            } else {
                return eventService.getEventsPreview();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all-events")
    public ResponseEntity<List<Events>> getAllEvents() {
        try {
            return eventService.getEvents();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Get event from DB
    @GetMapping("/get-event")
    private ResponseEntity<Events> eventProfile(@RequestParam("event-id") String eventId) {
        try {
            return eventService.eventProfile(UUID.fromString(eventId));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    //Event registration
    @PostMapping("/register")
    public ResponseEntity<Events> registerEvents(
            @Valid @RequestPart("jsonData") EventModel eventModel,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestParam("email-id") String emailId) {
        try {
            return eventService.registerEvents(eventModel, thumbnail, emailId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    //Add Contact Details to an event
    @PostMapping("/addContact")
    public ResponseEntity<List<EventContactModel>> addEventContact(
            @RequestPart(value = "imageFile") MultipartFile imageFile,
            @RequestPart(value = "jsonData") EventContactModel eventContact,
            @RequestParam("event-id") String eventId) {
        try {
            return eventService.addEventContact(eventContact, UUID.fromString(eventId), imageFile);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // Delete Event Contact
    @DeleteMapping("/deleteContact")
    public ResponseEntity<List<EventContactModel>> deleteEventContact(
            @RequestParam("event-id") String eventId,
            @RequestParam("contact") Long contact) {
        try {
            return eventService.deleteEventContact(UUID.fromString(eventId), contact);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // Edit Team Formation Guidelines
    @PostMapping("/edit-team-formation-guidelines")
    private ResponseEntity<String> editTeamFormationGuidelines(@RequestParam("event-id") String eventId,
                                                               @RequestBody String guidelines) {
        try {
            return eventService.editTeamFormationGuidelines(UUID.fromString(eventId), guidelines);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // Edit Rewards
    @PostMapping("/edit-rewards")
    private ResponseEntity<String> editRewards(@RequestParam("event-id") String eventId,
                                               @RequestBody String rewards) {
        try {
            return eventService.editRewards(UUID.fromString(eventId), rewards);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    // Edit Eligibility Criteria
    @PostMapping("/edit-eligibility-criteria")
    private ResponseEntity<String> editEligibilityCriteria(@RequestParam("event-id") String eventId,
                                                           @RequestBody String eligibilityCriteria) {
        try {
            return eventService.editEligibilityCriteria(UUID.fromString(eventId), eligibilityCriteria);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    // Add Downloadable Resource
    @PostMapping("/add-resource")
    private ResponseEntity<List<FileDto>> addResource(@RequestParam("event-id") String eventId,
                                                      @RequestPart(value = "imageFile") MultipartFile file) {
        try {
            return eventService.addResource(UUID.fromString(eventId), file);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    // Delete Resource
    @DeleteMapping("/delete-resource")
    private ResponseEntity<List<FileDto>> deleteResource(@RequestParam("event-id") String eventId,
                                                         @RequestParam("file-name") String fileName) {
        try {
            return eventService.deleteResource(UUID.fromString(eventId), fileName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    //Delete from DB
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteEvent(@RequestParam("event-id") String eventId) {
        return eventService.deleteEvent(UUID.fromString(eventId));
    }

    @PostMapping("/add-sponsor")
    public ResponseEntity<List<SponsorsModel>> addSponsor(@RequestParam("event-id") String eventId,
                                                          @RequestPart("image") MultipartFile file) {
        try {
            return eventService.addSponsor(UUID.fromString(eventId), file);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/delete-sponsor")
    private ResponseEntity<List<SponsorsModel>> deleteSponsor(@RequestParam("event-id") String eventId,
                                                              @RequestParam("file-name") String fileName) {
        try {
            return eventService.deleteSponsor(UUID.fromString(eventId), fileName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/add-special-guest")
    private ResponseEntity<List<SpecialGuestModel>> addSpecialGuest(
            @RequestParam("event-id") String eventId,
            @RequestPart("jsonData") SpecialGuestModel specialGuestModel,
            @RequestPart("image") MultipartFile image) {
        try {
            return eventService.addSpecialGuest(UUID.fromString(eventId), specialGuestModel, image);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/delete-special-guest")
    private ResponseEntity<List<SpecialGuestModel>> deleteSpecialGuest(@RequestParam("event-id") String eventId,
                                                                       @RequestParam("name") String name,
                                                                       @RequestParam("file-name") String fileName) {
        try {
            return eventService.deleteSpecialGuest(UUID.fromString(eventId), name, fileName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    // adding a new sub event
    @PostMapping("/add-sub-event")
    private ResponseEntity<List<SubEventsModel>> addNewSubEvent(@RequestParam("event-id") String eventId,
                                                                @RequestBody SubEventsModel subEventsModel) {

        try {
            return eventService.addNewSubEvent(UUID.fromString(eventId), subEventsModel);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    //delete sub event
    @DeleteMapping("/delete-sub-event")
    private ResponseEntity<List<SubEventsModel>> deleteSubEvent(@RequestParam("event-id") String eventId,
                                                                @RequestBody SubEventsModel subEventsModel) {
        try {
            return eventService.deleteSubEvent(UUID.fromString(eventId), subEventsModel);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/important-updates")
    private ResponseEntity<List<UpdateModel>> addUpdate(@RequestParam("event-id") String eventId,
                                                        @RequestBody String updateMessage) {
        try {
            return eventService.addUpdate(UUID.fromString(eventId), updateMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    //post social media links
    @PostMapping("/social-media-links")
    private ResponseEntity<SocialMediaLinksModel> addSocialMediaLinks(@RequestParam("event-id") String eventId,
                                                                      @RequestBody SocialMediaLinksModel socialMediaLinksModel) {
        try {
            return eventService.addSocialMediaLinks(UUID.fromString(eventId), socialMediaLinksModel);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/get-registrations")
    private ResponseEntity<List<Student>> getAllStudents(@RequestParam("event-id") String eventId) {
        try {
            return eventService.findAllStudentsRegisteredForEvent(eventId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }
}
