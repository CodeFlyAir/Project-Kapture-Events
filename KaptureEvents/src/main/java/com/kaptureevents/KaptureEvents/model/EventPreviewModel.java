package com.kaptureevents.KaptureEvents.model;

import com.kaptureevents.KaptureEvents.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventPreviewModel {
    private UUID event_id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String organizerName;
    private FileDto thumbnail;
}
