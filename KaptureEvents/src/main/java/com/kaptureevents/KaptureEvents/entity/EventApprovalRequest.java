package com.kaptureevents.KaptureEvents.entity;

import com.kaptureevents.KaptureEvents.model.EventStatusModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    @JoinColumn(name = "eventId")
    private Events event;

    @JdbcTypeCode(SqlTypes.JSON)
    private EventStatusModel.approvalStatus status;
}
