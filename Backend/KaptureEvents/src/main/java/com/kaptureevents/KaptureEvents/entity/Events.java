package com.kaptureevents.KaptureEvents.entity;

import com.kaptureevents.KaptureEvents.model.EventStatusModel;
import com.kaptureevents.KaptureEvents.model.SpecialGuestModel;
import com.kaptureevents.KaptureEvents.model.SubEventsModel;
import com.kaptureevents.KaptureEvents.model.UpdateModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;

    private List<String> sponsors;
    private Date startDate;
    private Date endDate;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<SpecialGuestModel> specialGuest;

    @ManyToOne(
            targetEntity = Society.class,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "id", nullable = false)
    private Long societyId;

    private Long contact;
    private String description;
    private String additionalDetails;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sub_events", nullable = false, unique = true)
    private List<SubEventsModel> subEvent;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<UpdateModel> updates;


    @JdbcTypeCode(SqlTypes.JSON)
    private List<EventStatusModel> eventStatus;
}
