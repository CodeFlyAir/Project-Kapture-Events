package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.entity.Admin;
import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.AdminModel;
import com.kaptureevents.KaptureEvents.service.AdminService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/registration")
    private ResponseEntity<Admin> registerAdmin(@Valid @RequestBody AdminModel adminModel) {
        try {
            return adminService.register(adminModel);
        }catch(Exception e){
            log.error(e.getMessage(), e);   //If User registration fails
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/pending-events")
    private ResponseEntity<List<Events>> pendingEvents(){
        try {
            return adminService.getPendingEvents();
        }catch(Exception e){
            log.error(e.getMessage(), e);   //If User registration fails
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/onHold-events")
    private  ResponseEntity<List<Events>> onHoldEvents(){
        try {
            return adminService.getonHoldEvents();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/event/{eventId}")
    private ResponseEntity<Events> getEvent(@PathVariable UUID eventId){
        try{
            return adminService.getEvent(eventId);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/event/change-status-to-on-hold")
    public ResponseEntity<String> changeEventStatusToOnHold(@RequestParam ("eventId") String eventId,
                                                            @RequestParam ("message") String message){
        adminService.changeEventStatusToHold(UUID.fromString(eventId),message);
        return new ResponseEntity<>("Event status changed to on Hold", HttpStatus.OK);
    }

    @PostMapping("/event/change-status-to-accept")
    public ResponseEntity<String> changeEventStatusToAccept(@RequestParam ("eventId") String eventId,
                                                            @RequestParam ("message") String message){
        adminService.changeEventStatusToAccept(UUID.fromString(eventId),message);
        return new ResponseEntity<>("Event status changed to Accepted",HttpStatus.OK);
    }

    @PostMapping("/event/change-status-to-reject")
    public ResponseEntity<String> changeStatusToReject(@RequestParam ("eventId") String eventId,
                                                       @RequestParam ("message") String message){
        adminService.changeEventStatusToReject(UUID.fromString(eventId),message);
        return new ResponseEntity<>("Event status changed to Rejected",HttpStatus.OK);
    }
}
