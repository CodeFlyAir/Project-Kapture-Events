package com.kaptureevents.KaptureEvents.repository;

import com.kaptureevents.KaptureEvents.entity.Society;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocietyRepository extends JpaRepository<Society,Long> {
    Optional<Society> findByEmailId(String email);

    Optional<Society> findByEmailIdAndPassword(String email, String password);
}
