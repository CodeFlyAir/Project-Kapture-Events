package com.kaptureevents.KaptureEvents.security;

import com.kaptureevents.KaptureEvents.handler.CustomAuthenticationSuccessHandler;
import com.kaptureevents.KaptureEvents.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Slf4j
@EnableWebSecurity
public class SecurityNew  {
    private UserModel userModel;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();

        // Configure OAuth 2.0 login with the success handler
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                            authorize.requestMatchers("/**").permitAll();
                            authorize.anyRequest().authenticated();
                        }
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(new CustomAuthenticationSuccessHandler()));


        return http.build();
    }
}
