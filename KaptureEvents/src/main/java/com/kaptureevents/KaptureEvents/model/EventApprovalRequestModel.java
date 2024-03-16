package com.kaptureevents.KaptureEvents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventApprovalRequestModel {
    private UUID eventId;
    private EventStatusModel.approvalStatus status;
}
