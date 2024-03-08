package com.kaptureevents.KaptureEvents.repository;

import com.kaptureevents.KaptureEvents.entity.SocietyAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocietyAuthRepository extends JpaRepository<SocietyAuth,Long> {
    Optional<SocietyAuth> findByEmail(String email);

  //  Boolean exitsByEmail(String email);
}
