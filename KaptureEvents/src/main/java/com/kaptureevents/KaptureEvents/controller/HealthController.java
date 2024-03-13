package com.kaptureevents.KaptureEvents.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class HealthController {
    @GetMapping("/check")
    public ResponseEntity<String> health(){
        return ResponseEntity.ok().body("Service Healthy");
    }
}
