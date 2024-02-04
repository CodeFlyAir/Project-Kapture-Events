package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.model.SocietyModel;
import com.kaptureevents.KaptureEvents.service.SocietyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/society")
@Slf4j
public class SocietyController {

    @Autowired
    private SocietyService societyService;

    // Society Registration
    @PostMapping("/register")
    public String registerSociety(@Valid @RequestBody SocietyModel societyModel) {
        try {
            societyService.registerSociety(societyModel);
            System.out.println("societyModel = " + societyModel);
            return "Society Registration Successful";
        } catch (Exception e) {
            log.error("Error occurred during society registration", e);
            return "Society Registration Failed";
        }
    }
}
