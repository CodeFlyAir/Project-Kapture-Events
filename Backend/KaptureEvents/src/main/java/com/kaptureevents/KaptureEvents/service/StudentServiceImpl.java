package com.kaptureevents.KaptureEvents.service;

import com.kaptureevents.KaptureEvents.entity.Student;
import com.kaptureevents.KaptureEvents.model.StudentModel;
import com.kaptureevents.KaptureEvents.repository.StudentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepository;

    // Save the student to DB
    @Override
    public void registerStudent(StudentModel studentModel) {
        Student student = new Student();

        // Sets the accepted student details to Student object
        student.setFirstName(studentModel.getFirstName());
        student.setLastName(studentModel.getLastName());
        student.setRoll(studentModel.getRoll());
        student.setEmail(studentModel.getEmail());
        student.setContact(studentModel.getContact());
        student.setGender(studentModel.getGender().toString().toUpperCase().charAt(0));

        studentRepository.save(student);    //Save the student to DB
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

}
