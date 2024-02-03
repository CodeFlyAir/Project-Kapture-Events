package com.kaptureevents.KaptureEvents.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Student {
    @Id
    private String email;
    private Long roll;
    private String firstName;
    private String lastName;
    private Long contact;
    private Character gender;
    private List<String> eventId;
}
