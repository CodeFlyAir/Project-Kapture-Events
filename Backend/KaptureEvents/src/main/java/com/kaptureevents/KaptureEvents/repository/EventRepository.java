package com.kaptureevents.KaptureEvents.repository;

import com.kaptureevents.KaptureEvents.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Events, UUID> {
    Optional<Events> findByName(String name);
}
