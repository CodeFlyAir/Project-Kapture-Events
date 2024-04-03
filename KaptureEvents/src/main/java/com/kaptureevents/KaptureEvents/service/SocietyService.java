package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Society;
import com.kaptureevents.KaptureEvents.model.SocietyModel;
import org.springframework.http.ResponseEntity;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SocietyService {
    void registerSociety(SocietyModel societyModel);

    ResponseEntity<Society> societyProfile(String email);

    ResponseEntity<Society> editSocietyDetails(String email, SocietyModel updatedSocietyModel);

    ResponseEntity<Boolean> deleteSociety(String email);

    ResponseEntity<Boolean> login(String email, String encryptedPassword) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
}
