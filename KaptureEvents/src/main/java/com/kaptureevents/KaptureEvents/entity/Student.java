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

@Entity
@Data
@NoArgsConstructor
public class Student {
    @Id
    @Email(message = "Invalid Email Format")
    @NotNull(message = "Email field should not be null")
    private String email;

    @NotNull(message = "Roll field should not be null")
    private Long roll;

    @NotNull
    private String firstName;
    private String lastName;

    private Long contact;

    private Character gender;

    private int year;

    @ElementCollection
    private List<String> registeredEventIds;

    public Student(StudentModel updatedStudentModel) {
        this.email=updatedStudentModel.getEmail();
        this.roll=updatedStudentModel.getRoll();
        this.firstName=updatedStudentModel.getFirstName();
        this.lastName=updatedStudentModel.getLastName();
        this.contact= updatedStudentModel.getContact();
        this.gender=updatedStudentModel.getGender();
        this.year=updatedStudentModel.getYear();
    }

}