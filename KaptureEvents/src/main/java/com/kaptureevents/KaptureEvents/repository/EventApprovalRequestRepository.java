package com.kaptureevents.KaptureEvents.repository;

import com.kaptureevents.KaptureEvents.entity.EventApprovalRequest;
import com.kaptureevents.KaptureEvents.model.EventStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventApprovalRequestRepository extends JpaRepository<EventApprovalRequest, UUID>{
    List<EventApprovalRequest> findAllByStatus(EventStatusModel.approvalStatus approvalStatus);
}
