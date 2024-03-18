package com.kaptureevents.KaptureEvents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentModel {
    private String email;
    private Long roll;
    private String firstName;
    private String lastName;
    private Long contact;
    private Character gender;
    private int year;
}
