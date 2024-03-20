package com.kaptureevents.KaptureEvents.repository;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.entity.Student;
import com.kaptureevents.KaptureEvents.entity.StudentEventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentEventRegistrationRepository
        extends JpaRepository<StudentEventRegistration,Long> {
    List<StudentEventRegistration> findByEvent(Events event);

    List<StudentEventRegistration> findByStudent(Student student);

    Optional<StudentEventRegistration> findByEventAndStudent(Events events, Student student);
}
