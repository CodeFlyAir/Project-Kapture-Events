package com.kaptureevents.KaptureEvents.entity;
import com.kaptureevents.KaptureEvents.model.StudentModel;

import jakarta.persistence.ElementCollection;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
public class Student {
    @Id
    @Email(message = "Invalid Email Format")
//    @NotBlank(message = "Email field should not be blank")
    @NotNull(message = "Email field should not be null")
    private String email;

//    @NotBlank(message = "Roll field should not be blank")
    @NotNull(message = "Roll field should not be null")
    private Long roll;

    @NotNull
//    @NotBlank
    private String firstName;
    private String lastName;

//    @Pattern(regexp = "\\d+",message = "Only numeric values are allowed")
    private Long contact;

//    @Pattern(regexp = "^[MF]$", message = "Gender must be 'M' or 'F'")
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
