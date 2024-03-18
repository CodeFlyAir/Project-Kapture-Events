package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.dto.LoginDto;
import com.kaptureevents.KaptureEvents.dto.RegisterDto;
import com.kaptureevents.KaptureEvents.entity.SocietyAuth;
import com.kaptureevents.KaptureEvents.repository.SocietyAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/path")
@CrossOrigin(
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*"
)
public class SocietyAuthController {
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private SocietyAuthRepository societyAuthRepository;

    @Autowired
    public SocietyAuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, SocietyAuthRepository societyAuthRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.societyAuthRepository = societyAuthRepository;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>("Society login succesful", HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        SocietyAuth societyAuth = new SocietyAuth();

        // Set the email and password using the provided data
        societyAuth.setEmail(registerDto.getEmail());
        societyAuth.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Save the societyAuth object
        societyAuthRepository.save(societyAuth);

        return new ResponseEntity<>("Society Registered Successfully", HttpStatus.OK);

    }


}

