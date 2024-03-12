package com.kaptureevents.KaptureEvents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminModel {
    private String email;
    private String firstName;
    private String lastName;
    private String position;
    private Long contact;
}
