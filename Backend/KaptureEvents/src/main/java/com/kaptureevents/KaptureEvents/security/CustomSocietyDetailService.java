package com.kaptureevents.KaptureEvents.security;

import com.kaptureevents.KaptureEvents.entity.SocietyAuth;
import com.kaptureevents.KaptureEvents.repository.SocietyAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomSocietyDetailService implements UserDetailsService {
    private SocietyAuthRepository societyAuthRepository;
    private PasswordEncoder passwordEncoder;
@Autowired
    public CustomSocietyDetailService(SocietyAuthRepository societyAuthRepository) {
        this.societyAuthRepository = societyAuthRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        SocietyAuth societyAuth = societyAuthRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email doesn't exist"));

        return User.builder()
                .username(societyAuth.getEmail())
                .password(societyAuth.getPassword())
                // Add any necessary roles or authorities
                .roles("USER")
                .build();

    }
}
