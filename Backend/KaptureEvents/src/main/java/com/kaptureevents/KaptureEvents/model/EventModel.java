package com.kaptureevents.KaptureEvents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventModel {

    private Date startDate;
    private Date endDate;
    private Long contact;
    private String description;
    private String additionalDetails;
}
