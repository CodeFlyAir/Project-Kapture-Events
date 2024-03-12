package com.kaptureevents.KaptureEvents.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubEventsModel {

    private String name;
    private String desc;
    private Date date;
    private ZonedDateTime time;
    private String venue;
}
