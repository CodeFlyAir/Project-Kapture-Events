package com.kaptureevents.KaptureEvents.repository;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.model.EventStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Events, UUID> {
    Optional<Events> findByName(String name);

    //EventStatusModel findByStatus(EventStatusModel.approvalStatus status);
    Optional<List<Events>> findByStartDateEquals(Date date);

    Optional<List<Events>> findByStartDateBetween(Date date, Date endDate);
}
