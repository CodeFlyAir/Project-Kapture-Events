package com.kaptureevents.KaptureEvents.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @Email(message = "Invalid Email Format")
    @NotNull(message = "Email field should not be null")
    private String email;

    @NotNull
    private String firstName;
    private String lastName;
    private String position;

    private Long contact;
}
