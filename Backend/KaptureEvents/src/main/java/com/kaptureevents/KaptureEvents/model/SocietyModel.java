package com.kaptureevents.KaptureEvents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SocietyModel {
    private Long id;
    private Long contact;
    private String emailId;
    private String societyName;
}
