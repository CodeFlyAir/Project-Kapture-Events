package com.kaptureevents.KaptureEvents.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaptureevents.KaptureEvents.model.EventStatusModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventApprovalRequest {
    @Id
    private UUID eventId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    private Events event;

    @JdbcTypeCode(SqlTypes.JSON)
    private EventStatusModel.approvalStatus status;

    private String message;
}
