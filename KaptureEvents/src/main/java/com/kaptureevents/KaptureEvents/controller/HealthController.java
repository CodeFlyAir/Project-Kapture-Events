package com.kaptureevents.KaptureEvents.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
@CrossOrigin(
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*",
        allowCredentials = "true",
        origins = {"http://localhost:5174", "http://kapture-events.onrender.com"}
)
public class HealthController {
    @GetMapping("/check")
    public ResponseEntity<String> health(){
        return ResponseEntity.ok().body("Service Healthy");
    }
}
