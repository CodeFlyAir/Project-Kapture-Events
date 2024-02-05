package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Society;
import com.kaptureevents.KaptureEvents.model.SocietyModel;
import org.springframework.http.ResponseEntity;

public interface SocietyService {
    void registerSociety(SocietyModel societyModel);

    ResponseEntity<Society> societyProfile(String email);

    ResponseEntity<Society> editSocietyDetails(String email, SocietyModel updatedSocietyModel);
}
