package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Admin;
import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.AdminModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<Admin> register(AdminModel adminModel);

    ResponseEntity<List<Events>> getPendingEvents();
}
