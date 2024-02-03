package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.entity.Student;
import com.kaptureevents.KaptureEvents.model.StudentModel;
import com.kaptureevents.KaptureEvents.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/registration")
    private String registerStudent(@Valid @RequestBody StudentModel studentModel) {
        try {
            studentService.registerStudent(studentModel);
            return "User Registration Successful";
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return "User Registration Failed";
    }
}
