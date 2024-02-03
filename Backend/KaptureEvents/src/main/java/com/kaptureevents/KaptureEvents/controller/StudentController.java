package com.kaptureevents.KaptureEvents.controller;

import com.kaptureevents.KaptureEvents.model.StudentModel;
import com.kaptureevents.KaptureEvents.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentModel studentModel;

    //Student Registration
    @PostMapping("/registration")
    private String registerStudent(@Valid @RequestBody StudentModel studentModel) {
        try {
            studentService.registerStudent(studentModel);
            return "User Registration Successful";
        }catch(Exception e){
            log.error(e.getMessage(), e);   //If User registration fails
        }
        return "User Registration Failed";
    }

    @GetMapping("/profile")
    private StudentModel studentProfile(){
        return studentService.studentProfile(studentModel.getEmail());
    }
}
