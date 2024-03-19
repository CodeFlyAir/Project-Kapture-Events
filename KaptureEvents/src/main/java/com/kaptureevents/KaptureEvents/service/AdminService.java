package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Admin;
import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.AdminModel;
import com.kaptureevents.KaptureEvents.model.EventStatusModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    ResponseEntity<Admin> register(AdminModel adminModel);

    ResponseEntity<List<Events>> getPendingEvents();

    ResponseEntity<List<Events>> getOnHoldEvents();

    ResponseEntity<Object> changeEventStatus(UUID uuid, EventStatusModel.approvalStatus status, String message);
}
