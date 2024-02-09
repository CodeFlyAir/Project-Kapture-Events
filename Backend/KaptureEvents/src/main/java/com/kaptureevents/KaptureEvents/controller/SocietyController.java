package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.entity.Society;
import com.kaptureevents.KaptureEvents.model.SocietyModel;
import com.kaptureevents.KaptureEvents.service.SocietyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            log.error(e.getMessage(), e);
        }
        return "Society Registration Failed";
    }

    //ToDo: Remove the id from path variable and get it
    // from Animesh (for currently logged user)

    @GetMapping("/profile/{email}")
    private ResponseEntity<Society> societyProfile(@PathVariable String email){
        return societyService.societyProfile(email);
    }

    //ToDo: Remove id from Path Variable and get it from
    // Animesh for currently logged user
    @PutMapping("/profile/edit/{email}")
    public ResponseEntity<Society> updateSocietyDetails(@PathVariable String email,
                                                        @RequestBody SocietyModel updatedSocietyModel){
        return societyService.editSocietyDetails(email,updatedSocietyModel);
    }


    @DeleteMapping("/profile/delete/{email}")
    public ResponseEntity<Boolean> deleteSociety(@PathVariable String email){
        return societyService.deleteSociety(email);
    }
}