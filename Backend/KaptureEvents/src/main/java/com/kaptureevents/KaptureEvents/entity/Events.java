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
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID event_id;

    @Column(nullable = false,unique = true)
    private String name;
    private Date startDate;
    private Date endDate;
    private Long contact;
    private String description;
    private String additionalDetails;

    private List<String> sponsors;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<SpecialGuestModel> specialGuest;

    @ManyToOne(
            targetEntity = Society.class,
            cascade = CascadeType.ALL
    )
    @JoinColumn(nullable = false,referencedColumnName = "id")
    private Society societyId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sub_events")
    private List<SubEventsModel> subEvent;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<UpdateModel> updates;


    @JdbcTypeCode(SqlTypes.JSON)
    private List<EventStatusModel> eventStatus;
}