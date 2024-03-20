package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.entity.Student;
import com.kaptureevents.KaptureEvents.entity.StudentEventRegistration;
import com.kaptureevents.KaptureEvents.model.StudentEventRegistrationModel;
import com.kaptureevents.KaptureEvents.model.StudentModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentService {
    ResponseEntity<String> registerStudent(StudentModel studentModel, String eventId);

    ResponseEntity<List<Events>> findAllRegisteredEvents(String studentEmail);

    ResponseEntity<Student> studentProfile(String email);

    ResponseEntity<Student> editStudentDetails(String email, StudentModel updatedStudentModel);

    ResponseEntity<Boolean> deleteStudent(String email);

    ResponseEntity<Boolean> checkRegistration(String eventId, String emailId);

    ResponseEntity<List<StudentEventRegistrationModel>> getAllRegistrations();
}
