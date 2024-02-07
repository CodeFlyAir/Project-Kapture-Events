package com.kaptureevents.KaptureEvents.entity;
import com.kaptureevents.KaptureEvents.model.StudentModel;

import jakarta.persistence.ElementCollection;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
public class Student {
    @Id
    @Email
    private String email;
    @NotNull
    private Long roll;
    private String firstName;
    private String lastName;
    private Long contact;
    private Character gender;
    @ElementCollection
    private List<String> eventId;

    public Student(StudentModel updatedStudentModel) {
        this.email=updatedStudentModel.getEmail();
        this.roll=updatedStudentModel.getRoll();
        this.firstName=updatedStudentModel.getFirstName();
        this.lastName=updatedStudentModel.getLastName();
        this.contact= updatedStudentModel.getContact();
        this.gender=updatedStudentModel.getGender();
    }

}
