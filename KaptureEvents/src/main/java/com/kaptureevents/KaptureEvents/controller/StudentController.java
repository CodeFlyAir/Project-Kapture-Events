package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.entity.Events;
import com.kaptureevents.KaptureEvents.entity.Student;
import com.kaptureevents.KaptureEvents.entity.StudentEventRegistration;
import com.kaptureevents.KaptureEvents.model.StudentEventRegistrationModel;
import com.kaptureevents.KaptureEvents.model.StudentModel;
import com.kaptureevents.KaptureEvents.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@CrossOrigin(
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*"
)
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;

    //Student Registration
    @PostMapping("/register")
    private ResponseEntity<String> registerStudent(@Valid @RequestBody StudentModel studentModel,
                                                   @RequestParam("event-id") String eventId) {
        try {
            return studentService.registerStudent(studentModel, eventId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);   //If User registration fails
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/check-registration")
    private ResponseEntity<Boolean> checkRegistration(@RequestParam("event-id") String eventId,
                                                      @RequestParam("email-id") String emailId) {
        try {
            return studentService.checkRegistration(eventId, emailId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);   //If User registration fails
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/get-all-registrations")
    private ResponseEntity<List<StudentEventRegistrationModel>> getAllRegistrations() {
        try {
            return studentService.getAllRegistrations();
        } catch (Exception e) {
            log.error(e.getMessage(), e);   //If User registration fails
        }
        return ResponseEntity.internalServerError().build();
    }


    //TODO : Remove email from Path Variable and get it from
    // Spring Security for currently logged in user
    @GetMapping("/profile/{email}")
    private ResponseEntity<Student> studentProfile(@PathVariable String email) {
        return studentService.studentProfile(email);
    }

    //TODO : Remove email from Path Variable and get it from
    // Spring Security for currently logged in user
    @PutMapping("/profile/edit/{email}")
    public ResponseEntity<Student> updateStudentDetails(@PathVariable String email,
                                                        @RequestBody StudentModel updatedStudentModel) {
        try {
            return studentService.editStudentDetails(email, updatedStudentModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.internalServerError().build();
    }


    //TODO : Remove email from Path Variable and get it from
    // Spring Security for currently logged in user
    @DeleteMapping("/profile/delete/{email}")
    public ResponseEntity<Boolean> deleteStudent(@PathVariable String email) {
        try {
            return studentService.deleteStudent(email);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.internalServerError().build();
    }

    //All registered events for a particular student
    @GetMapping("/getAllEvents")
    public ResponseEntity<List<Events>> getAllEvents(@RequestParam("email-id") String email) {
        try {
            return studentService.findAllRegisteredEvents(email);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.internalServerError().build();
    }

}