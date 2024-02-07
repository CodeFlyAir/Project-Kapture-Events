package com.kaptureevents.KaptureEvents.entity;

import com.kaptureevents.KaptureEvents.model.SocietyModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
//@Table(name = "society")
public class Society {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long contact;

    @Column(name = "email_id", nullable = false, unique = true)
    private String emailId;
    private String societyName;

    @ElementCollection
    private List<String> events;

    public Society(SocietyModel updatedSocietyModel){
        this.contact=updatedSocietyModel.getContact();
        this.emailId=updatedSocietyModel.getEmailId();
        this.societyName= updatedSocietyModel.getSocietyName();
    }

    public Society() {

    }
}
