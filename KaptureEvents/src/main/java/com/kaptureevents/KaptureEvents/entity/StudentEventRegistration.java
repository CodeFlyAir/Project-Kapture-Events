package com.kaptureevents.KaptureEvents.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_email", referencedColumnName = "email")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private Events event;

}
