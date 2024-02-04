package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EventController {

    UserModel userModel=UserModel.getInstance();
    @GetMapping("/events")
    public String getEvents() {
        return userModel.getName();

    }
}
