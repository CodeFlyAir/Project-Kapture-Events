package com.kaptureevents.KaptureEvents.model;

import com.kaptureevents.KaptureEvents.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialGuestModel {
    private FileDto image;
    private String name;
    private String post;
    private Date date;
    private Timestamp time;
    private String venue;
}
