package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.entity.Student;
import com.kaptureevents.KaptureEvents.entity.StudentEventRegistration;
import com.kaptureevents.KaptureEvents.model.StudentEventRegistrationModel;
import com.kaptureevents.KaptureEvents.model.StudentModel;
import com.kaptureevents.KaptureEvents.repository.EventRepository;
import com.kaptureevents.KaptureEvents.repository.StudentEventRegistrationRepository;
import com.kaptureevents.KaptureEvents.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private StudentEventRegistrationRepository studentEventRegistrationRepository;

    // Save the student to DB
    @Override
    public ResponseEntity<String> registerStudent(StudentModel studentModel, String eventId) {
        // Retrieve the event from the database
        try {
            Optional<Events> optionalEvent = eventRepository.findById(UUID.fromString(eventId));
            if (optionalEvent.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Events event = optionalEvent.get();

            // Create a new student entity
            Student student = new Student();
            student.setFirstName(studentModel.getFirstName());
            student.setLastName(studentModel.getLastName());
            student.setRoll(studentModel.getRoll());
            student.setEmail(studentModel.getEmail());
            student.setContact(studentModel.getContact());
            student.setGender(studentModel.getGender().toString().toUpperCase().charAt(0));

            // Save the student to the database
            studentRepository.save(student);

            // Create a new StudentEventRegistration entity to link the student and event
            StudentEventRegistration registration = new StudentEventRegistration();
            registration.setStudent(student);
            registration.setEvent(event);

            // Save the registration to the database
            studentEventRegistrationRepository.save(registration);

            // Return a ResponseEntity indicating success
            return ResponseEntity.ok("Student registered for event successfully");
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already registered for the event");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Events>> findAllRegisteredEvents(String studentEmail) {
        try {
            // Retrieve the student from the database
            Optional<Student> optionalStudent = studentRepository.findById(studentEmail);
            if (optionalStudent.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Student student = optionalStudent.get();

            // Retrieve all registrations for the student
            List<StudentEventRegistration> registrations = studentEventRegistrationRepository.findByStudent(student);

            // Extract the events from the registrations
            List<Events> registeredEvents = registrations.stream()
                    .map(StudentEventRegistration::getEvent)
                    .collect(Collectors.toList());

            // Return a ResponseEntity with the list of registered events
            return ResponseEntity.ok(registeredEvents);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }


    //Get Student details from DB
    @Override
    public ResponseEntity<Student> studentProfile(String email) {
        Optional<Student> studentOptional = studentRepository.findById(email);

        if (studentOptional.isPresent()) {      // If student is present in DB
            Student student = studentOptional.get();
            return new ResponseEntity<>(student, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // If student is not present in DB
        }
    }

    @Override
    public ResponseEntity<Student> editStudentDetails(String email, StudentModel updatedStudentModel) {
        // Retrieve existing student from the database
        Student existingStudent = studentRepository.findById(email).orElse(null);

        if (existingStudent == null) {
            // Return not found response if the student is not present
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Update existing student with the new details
        BeanUtils.copyProperties(updatedStudentModel, existingStudent, "email", "eventId");

        // Save the updated student back to the database
        studentRepository.save(existingStudent);

        // Return the updated student details
        return new ResponseEntity<>(new Student(updatedStudentModel), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Boolean> deleteStudent(String email) {
        if (studentRepository.existsById(email)) {
            studentRepository.deleteById(email);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Boolean> checkRegistration(String eventId, String emailId) {
        try {
            Optional<Events> event = eventRepository.findById(UUID.fromString(eventId));
            Optional<Student> student = studentRepository.findById(emailId);

            if (event.isPresent() && student.isPresent()) {
                Optional<StudentEventRegistration> registration = studentEventRegistrationRepository.findByEventAndStudent(event.get(), student.get());

                if (registration.isPresent()) {
                    return ResponseEntity.ok(true);
                }
            }
            return ResponseEntity.ok(false);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<StudentEventRegistrationModel>> getAllRegistrations() {
        try {
            List<StudentEventRegistration> registration = studentEventRegistrationRepository.findAll();
            List<StudentEventRegistrationModel> registrationModels = new ArrayList<>();

            for (StudentEventRegistration studentEventRegistration : registration) {
                StudentEventRegistrationModel model = new StudentEventRegistrationModel();
                model.setEventId(studentEventRegistration.getEvent().getEvent_id());
                model.setEmailId(studentEventRegistration.getStudent().getEmail());

                registrationModels.add(model);
            }
            return ResponseEntity.ok(registrationModels);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
