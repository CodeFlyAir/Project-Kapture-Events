package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Admin;
import com.kaptureevents.KaptureEvents.entity.EventApprovalRequest;
import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.AdminModel;
import com.kaptureevents.KaptureEvents.model.EventStatusModel;
import com.kaptureevents.KaptureEvents.repository.AdminRepository;
import com.kaptureevents.KaptureEvents.repository.EventApprovalRequestRepository;
import com.kaptureevents.KaptureEvents.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EventApprovalRequestRepository eventApprovalRequestRepository;

    @Autowired
    private EventRepository eventRepository;

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

            List<EventApprovalRequest> requests =
                    eventApprovalRequestRepository.findAllByStatus(EventStatusModel.approvalStatus.pending);

            for (EventApprovalRequest request : requests) {
                Events event = request.getEvent();
                events.add(event);
            }
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Events>> getOnHoldEvents() {
        try {
            List<Events> events = new ArrayList<>();
            List<EventApprovalRequest> requests =
                    eventApprovalRequestRepository.findAllByStatus(EventStatusModel.approvalStatus.onHold);
            for (EventApprovalRequest request : requests) {
                events.add(request.getEvent());
            }
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Object> changeEventStatus(UUID uuid, EventStatusModel.approvalStatus status, String message) {
        return changeEventStatusHelper(uuid, message, status);
    }

    private ResponseEntity<Object> changeEventStatusHelper(UUID uuid, String message, EventStatusModel.approvalStatus status) {
        try {
            Optional<EventApprovalRequest> eventApprovalRequestOptional = eventApprovalRequestRepository.findById(uuid);
            Optional<Events> eventsOptional = eventRepository.findById(uuid);

            if (eventApprovalRequestOptional.isPresent() && eventsOptional.isPresent()) {
                EventApprovalRequest eventApprovalRequest = eventApprovalRequestOptional.get();
                eventApprovalRequest.setStatus(status);
                eventApprovalRequest.setMessage(message);

                EventStatusModel eventStatusModel = new EventStatusModel();
                eventStatusModel.setDate(new Date());
                eventStatusModel.setStatus(status);
                eventStatusModel.setMessage(message);

                Events events = eventsOptional.get();
                List<EventStatusModel> statusModelList = events.getEventStatus();
                statusModelList.add(eventStatusModel);
                events.setEventStatus(statusModelList);

                eventRepository.save(events);

                if (status == EventStatusModel.approvalStatus.onHold || status == EventStatusModel.approvalStatus.pending) {
                    eventApprovalRequestRepository.save(eventApprovalRequest);
                } else {
                    eventApprovalRequestRepository.deleteById(eventApprovalRequest.getEventId());
                }

                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return ResponseEntity.internalServerError().build();
    }

}
