package com.kaptureevents.KaptureEvents.repository;

import com.kaptureevents.KaptureEvents.entity.Society;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocietyRepository extends JpaRepository<Society,Long> {
}
