package com.kaptureevents.KaptureEvents.model;

import com.kaptureevents.KaptureEvents.entity.Events;
import jakarta.persistence.Entity;
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
