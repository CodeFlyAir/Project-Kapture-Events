package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Student;
import com.kaptureevents.KaptureEvents.model.StudentModel;
import com.kaptureevents.KaptureEvents.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepository;
    @Override
    public void registerStudent(StudentModel studentModel) {
        Student student = new Student();
        student.setFirstName(studentModel.getFirstName());
        student.setLastName(studentModel.getLastName());
        student.setRoll(studentModel.getRoll());
        student.setEmail(studentModel.getEmail());
        student.setContact(studentModel.getContact());
        student.setGender(studentModel.getGender());

        studentRepository.save(student);
    }
}
