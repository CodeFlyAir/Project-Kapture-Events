package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.entity.Admin;
import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.AdminModel;
import com.kaptureevents.KaptureEvents.model.EventStatusModel;
import com.kaptureevents.KaptureEvents.service.AdminService;
import com.kaptureevents.KaptureEvents.service.EventService;
import com.kaptureevents.KaptureEvents.utils.EventStatusConverter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@Slf4j
@CrossOrigin(
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*"
)

public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private EventService eventService;

    @PostMapping("/registration")
    private ResponseEntity<Admin> registerAdmin(@Valid @RequestBody AdminModel adminModel) {
        try {
            return adminService.register(adminModel);
        }catch(Exception e){
            log.error(e.getMessage(), e);   //If User registration fails
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/events/pending-events")
    private ResponseEntity<List<Events>> pendingEvents(){
        try {
            return adminService.getPendingEvents();
        }catch(Exception e){
            log.error(e.getMessage(), e);   //If User registration fails
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/events/on-hold-events")
    private  ResponseEntity<List<Events>> onHoldEvents(){
        try {
            return adminService.getOnHoldEvents();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/events/get-event")
    private ResponseEntity<Events> getEvent(@RequestParam("event-id") String eventId){
        try{
            return eventService.eventProfile(UUID.fromString(eventId));
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/event/change-event-status")
    public ResponseEntity<Object> changeEventStatus(@RequestParam ("event-id") String eventId,
                                                    @RequestParam (value = "status") String statusText,
                                                    @RequestParam (value = "message",required = false) String message){
        try {
            EventStatusModel.approvalStatus status = EventStatusConverter.converter(statusText);
            if(status == EventStatusModel.approvalStatus.unknown){
                return ResponseEntity.badRequest().build();
            }

            return adminService.changeEventStatus(UUID.fromString(eventId),status,message);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
