package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Admin;
import com.kaptureevents.KaptureEvents.entity.EventApprovalRequest;
import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.AdminModel;
import com.kaptureevents.KaptureEvents.repository.AdminRepository;
import com.kaptureevents.KaptureEvents.repository.EventApprovalRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EventApprovalRequestRepository eventApprovalRequestRepository;

    @Override
    public ResponseEntity<Admin> register(AdminModel adminModel) {
        Admin admin = new Admin();
        admin.setFirstName(adminModel.getFirstName());
        admin.setLastName(adminModel.getLastName());
        admin.setEmail(adminModel.getEmail());
        admin.setPosition(adminModel.getPosition());
        admin.setContact(adminModel.getContact());

        try {
            return ResponseEntity.ok(adminRepository.save(admin));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Events>> getPendingEvents() {
        try {
            List<Events> events = new ArrayList<>();

            List<EventApprovalRequest> requests=eventApprovalRequestRepository.findAll();

            for(EventApprovalRequest request : requests) {
                Events event = request.getEvent();
                events.add(event);
            }
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
