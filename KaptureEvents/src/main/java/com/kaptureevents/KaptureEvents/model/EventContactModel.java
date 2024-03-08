package com.kaptureevents.KaptureEvents.model;

import com.kaptureevents.KaptureEvents.dto.FileDto;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventContactModel {
    private String name;
    private String post;
    private Long contact;
    @Email
    private String email;
    private FileDto image;
}
