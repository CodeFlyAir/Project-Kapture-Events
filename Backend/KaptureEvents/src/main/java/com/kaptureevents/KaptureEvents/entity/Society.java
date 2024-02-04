package com.kaptureevents.KaptureEvents.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
//@Table(name = "society")
public class Society {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long contact;
    private String emailId;
    private String societyName;
}
