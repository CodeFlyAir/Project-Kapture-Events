package com.kaptureevents.KaptureEvents.entity;

import com.kaptureevents.KaptureEvents.model.*;
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
    private long event_id;

    private Date startDate;
    private Date endDate;
    private String description;
    private String additionalDetails;

    private List<String> sponsors;

    private List<EventContactModel> contact;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<SpecialGuestModel> specialGuest;

    @ManyToOne(
            targetEntity = Society.class,
            cascade = CascadeType.ALL
    )
   @JoinColumn(name = "id", nullable = false)
   private Society societyId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sub_events")
    private List<SubEventsModel> subEvent;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<UpdateModel> updates;


    @JdbcTypeCode(SqlTypes.JSON)
    private List<EventStatusModel> eventStatus;
}
