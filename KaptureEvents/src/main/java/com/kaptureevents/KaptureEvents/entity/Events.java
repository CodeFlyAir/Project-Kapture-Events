package com.kaptureevents.KaptureEvents.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kaptureevents.KaptureEvents.dto.FileDto;
import com.kaptureevents.KaptureEvents.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Date;
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

    @Column(nullable = false, unique = true)
    private String name;
    private Date startDate;
    private Date endDate;
    private String description;
    private String organizerName;


    @JdbcTypeCode(SqlTypes.JSON)
    private FileDto thumbnail;

    @JdbcTypeCode(SqlTypes.JSON)
    private EventAdditionalDetailsModel additionalDetails;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<SponsorsModel> sponsors;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<EventContactModel> contact;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<SpecialGuestModel> specialGuest;

    @ManyToOne(
            targetEntity = Society.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private Society societyId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sub_events")
    private List<SubEventsModel> subEvent;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<UpdateModel> updates;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<EventStatusModel> eventStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    private SocialMediaLinksModel socialMedia;


    @OneToOne(mappedBy = "event", orphanRemoval = true)
    private EventApprovalRequest approvalRequest;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentEventRegistration> registeredStudents;

}