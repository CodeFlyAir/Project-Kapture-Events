package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.*;
import com.kaptureevents.KaptureEvents.service.EventService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
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

    //Event registration
    @PostMapping("/register/{emailId}")
    public ResponseEntity<Events> registerEvents(
            @Valid @RequestPart("jsonData") EventModel eventModel,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @PathVariable String emailId) {
        try {
            return eventService.registerEvents(eventModel, thumbnail, emailId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // Delete Event Contact
    @DeleteMapping("/{eventName}/deleteContact/{contact}")
    public ResponseEntity<Events> deleteEventContact(
            @PathVariable("eventName") String eventName,
            @PathVariable("contact") Long contact) {
        try {
            return eventService.deleteEventContact(eventName, contact);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    //Get event from DB
    @GetMapping("/{eventName}")
    private ResponseEntity<Events> eventProfile(@PathVariable String eventName) {
        try {
            return eventService.eventProfile(eventName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // Edit Team Formation Guidelines
    @PostMapping("/{eventName}/edit-team-formation-guidelines")
    private ResponseEntity<String> editTeamFormationGuidelines(@PathVariable String eventName,
                                                               @RequestBody String guidelines) {
        try {
            return eventService.editTeamFormationGuidelines(eventName, guidelines);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // Edit Rewards
    @PostMapping("/{eventName}/edit-rewards")
    private ResponseEntity<String> editRewards(@PathVariable String eventName,
                                               @RequestBody String rewards) {
        try {
            return eventService.editRewards(eventName, rewards);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    // Edit Eligibility Criteria
    @PostMapping("/{eventName}/edit-eligibility-criteria")
    private ResponseEntity<String> editEligibilityCriteria(@PathVariable String eventName,
                                                           @RequestBody String eligibilityCriteria) {
        try {
            return eventService.editEligibilityCriteria(eventName, eligibilityCriteria);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    // Add Downloadable Resource
    @PostMapping("/{eventName}/add-resource")
    private ResponseEntity<Events> addResource(@PathVariable String eventName,
                                               @RequestPart(value = "imageFile") MultipartFile file) {
        try {
            return eventService.addResource(eventName, file);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    // Delete Resource
    @DeleteMapping("/{eventName}/delete-resource/{fileName}")
    private ResponseEntity<Events> deleteResource(@PathVariable("eventName") String eventName,
                                                  @PathVariable("fileName") String fileName) {
        try {
            return eventService.deleteResource(eventName, fileName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    //Delete from DB
    @DeleteMapping("/delete/{eventName}")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable String eventName) {
        return eventService.deleteEvent(eventName);
    }

    @PostMapping("/{eventName}/add-sponsor")
    private ResponseEntity<Events> addSponsor(@PathVariable String eventName,
                                              @RequestPart("image") MultipartFile file) {
        try {
            return eventService.addSponsor(eventName, file);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{eventName}/delete-sponsor/{fileName}")
    private ResponseEntity<Events> deleteSponsor(@PathVariable("eventName") String eventName,
                                                 @PathVariable("fileName") String fileName) {
        try {
            return eventService.deleteSponsor(eventName, fileName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/{eventName}/add-special-guest")
    private ResponseEntity<Events> addSpecialGuest(
            @PathVariable String eventName,
            @RequestPart("jsonData") SpecialGuestModel specialGuestModel,
            @RequestPart("image") MultipartFile image) {
        try {
            return eventService.addSpecialGuest(eventName, specialGuestModel, image);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{eventName}/delete-special-guest")
    private ResponseEntity<Events> deleteSpecialGuest(@PathVariable("eventName") String eventName,
                                                      @RequestBody SpecialGuestModel specialGuestModel) {
        try {
            return eventService.deleteSpecialGuest(eventName, specialGuestModel);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    // adding a new sub event
    @PostMapping("/{eventName}/add-new-sub-event")
    private ResponseEntity<Events> addNewSubEvent(@Valid
                                                  @PathVariable String eventName,
                                                  @RequestBody SubEventsModel subEventsModel) {

        try {
            return eventService.addNewSubEvent(eventName, subEventsModel);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/{eventName}/important-updates")
    private ResponseEntity<Events> addUpdate(@Valid
                                             @PathVariable String eventName,
                                             @RequestBody UpdateModel updateModel) {
        try {
            return eventService.addUpdate(eventName, updateModel);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    //delete sub event
    @DeleteMapping("/{eventName}/delete-sub-event")
    private ResponseEntity<Events> deleteSubEvent(@PathVariable("eventName") String eventName,
                                                  @RequestBody SubEventsModel subEventsModel) {
        try {
            return eventService.deleteSubEvent(eventName, subEventsModel);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    //post social media links
    @PostMapping("/{eventName}/social-media-links")
    private ResponseEntity<Events> addSocialMediaLinks(@Valid
                                                       @PathVariable String eventName,
                                                       @RequestBody SocialMediaLinksModel socialMediaLinksModel) {
        try {
            return eventService.addSocialMediaLinks(eventName, socialMediaLinksModel);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }
}
