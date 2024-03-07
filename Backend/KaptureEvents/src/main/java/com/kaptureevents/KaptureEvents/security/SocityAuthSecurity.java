package com.kaptureevents.KaptureEvents.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class SocityAuthSecurity {
    private UserDetailsService societyAuthUserDetailsService;
    private JWTAuthEntryPoint authEntryPoint;

    public SocityAuthSecurity(UserDetailsService societyAuthUserDetailsService, JWTAuthEntryPoint authEntryPoint) {
        this.societyAuthUserDetailsService = societyAuthUserDetailsService;
        this.authEntryPoint = authEntryPoint;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();

        // Configure OAuth 2.0 login with the success handler
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {

                           // authorize.requestMatchers("/**").permitAll();
                            authorize.requestMatchers(HttpMethod.POST, "/api/path/register").permitAll();
                            authorize.requestMatchers(HttpMethod.POST, "/api/path/login").permitAll();


                            authorize.anyRequest().authenticated();

                        }

                );


        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(

            AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}



