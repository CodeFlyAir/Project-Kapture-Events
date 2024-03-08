package com.kaptureevents.KaptureEvents.model;

import com.kaptureevents.KaptureEvents.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventAdditionalDetailsModel {
    private String teamFormationGuidelines;
    private String eligibilityCriteria;
    private String rewards;
    private List<FileDto> resources;
}