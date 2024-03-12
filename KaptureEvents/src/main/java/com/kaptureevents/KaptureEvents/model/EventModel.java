package com.kaptureevents.KaptureEvents.model;

import com.kaptureevents.KaptureEvents.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
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
    private FileDto thumbnail;

    private List<SponsorsModel> sponsors;
    private List<SpecialGuestModel> specialGuest;
    private List<SubEventsModel> subEvent;
    private List<UpdateModel> updates;
    private List<EventStatusModel> eventStatus;
    private SocialMediaLinksModel socialMedia;
}
