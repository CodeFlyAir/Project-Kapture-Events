package com.kaptureevents.KaptureEvents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventModel {

    private String name;
    private Date startDate;
    private Date endDate;
    private List<EventContactModel> contact;
    private String description;
    private EventAdditionalDetailsModel additionalDetails;

    private List<String> sponsors;
    private List<SpecialGuestModel> specialGuest;
    private List<SubEventsModel> subEvent;
    private List<UpdateModel> updates;
    private List<EventStatusModel> eventStatus;
}
