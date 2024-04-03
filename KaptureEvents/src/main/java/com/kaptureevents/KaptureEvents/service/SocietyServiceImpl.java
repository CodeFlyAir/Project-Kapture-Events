package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Society;
import com.kaptureevents.KaptureEvents.model.SocietyModel;
import com.kaptureevents.KaptureEvents.repository.SocietyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class SocietyServiceImpl implements SocietyService {

    @Autowired
    private SocietyRepository societyRepository;

    @Override
    public void registerSociety(SocietyModel societyModel) {
        Society society = new Society();

        society.setSocietyName(societyModel.getSocietyName());
        society.setEmailId(societyModel.getEmailId());
        society.setContact(societyModel.getContact());
        society.setPassword(societyModel.getPassword());

        societyRepository.save(society); //saving to DB
    }

    //Getting society details from DB
    @Override
    public ResponseEntity<Society> societyProfile(String email) {
        Optional<Society> societyOptional = societyRepository.findByEmailId(email);

        if (societyOptional.isPresent()) {
            Society society = societyOptional.get();
            return new ResponseEntity<>(society, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Society> editSocietyDetails(String email, SocietyModel updatedSocietyModel) {

        //Retrieve existing student from database
        Society existingSociety = societyRepository.findByEmailId(email).orElse(null);
        if (existingSociety == null) {
            //returning not found response if society not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //updating existing student with new details
        BeanUtils.copyProperties(updatedSocietyModel, existingSociety, "id");

        //saving to DB
        societyRepository.save(existingSociety);

        //returning updated student
        return new ResponseEntity<>(new Society(updatedSocietyModel), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> deleteSociety(String email) {
        Optional<Society> society = societyRepository.findByEmailId(email);
        if (society.isPresent()) {
            societyRepository.deleteById(society.get().getId());
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Boolean> login(String email, String password) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Optional<Society> society = societyRepository.findByEmailId(email);

        if (society.isPresent()) {
            log.info("Password: " + password);
            if (verifyPassword(password, society.get().getPassword()))
                return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null) {
            return false;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        log.info(String.valueOf(encoder.matches(rawPassword, hashedPassword)));
        return encoder.matches(rawPassword, hashedPassword);
    }
}

