package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.EventContactModel;
import com.kaptureevents.KaptureEvents.model.EventModel;
import com.kaptureevents.KaptureEvents.model.SpecialGuestModel;
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

    @GetMapping("/")
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
    @GetMapping("/{eventName}")
    private ResponseEntity<Events> eventProfile(@PathVariable String name) {
        try {
            return eventService.eventProfile(name);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{eventName}/edit-team-formation-guidelines")
    private ResponseEntity<String> editTeamFormationGuidelines(@PathVariable String eventName,
                                                               @RequestBody String guidelines) {
        try {
            return eventService.editTeamFormationGuidelines(eventName, guidelines);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

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

}
