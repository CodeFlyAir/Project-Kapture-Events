package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.entity.Admin;
import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.AdminModel;
import com.kaptureevents.KaptureEvents.service.AdminService;
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

    @GetMapping("/event/{eventId}")
    private ResponseEntity<Events> getEvent(@PathVariable UUID eventId){
        try{
            return adminService.getEvent(eventId);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }
}
