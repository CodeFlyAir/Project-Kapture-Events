package com.kaptureevents.KaptureEvents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@SpringBootApplication
@RestController
public class KaptureEventsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KaptureEventsApplication.class, args);
	}

}
