package com.kaptureevents.KaptureEvents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialGuestModel {
    private String imageUrl;
    private String name;
    private String post;
    private Date date;
    private Timestamp time;
    private String venue;
}
