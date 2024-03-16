package com.kaptureevents.KaptureEvents.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventStatusModel {

    private Date date;

    private approvalStatus status;

    public enum approvalStatus{
        pending,
        rejected,
        approved
    }
}
