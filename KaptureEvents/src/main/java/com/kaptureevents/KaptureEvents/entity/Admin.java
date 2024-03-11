package com.kaptureevents.KaptureEvents.entity;

import com.kaptureevents.KaptureEvents.model.EventModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @Email(message = "Invalid Email Format")
    @NotNull(message = "Email field should not be null")
    private String email;

    @NotNull(message = "Employee ID field should not be null")
    private Long empId;

    @NotNull
    private String firstName;
    private String lastName;

    private Long contact;

    private List<EventModel> eventApprovalRequests;
}
