package com.kaptureevents.KaptureEvents.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Entity
@Data
public class Student {
    @Id
    private String email;
    @NotNull
    private Long roll;
    private String firstName;
    private String lastName;
    private Long contact;
    private Character gender;
    @ElementCollection
    private List<String> eventId;
}
