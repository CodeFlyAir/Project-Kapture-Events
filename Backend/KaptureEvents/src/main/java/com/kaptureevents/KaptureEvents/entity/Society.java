package com.kaptureevents.KaptureEvents.entity;

import com.kaptureevents.KaptureEvents.model.SocietyModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
//@Table(name = "society")
public class Society {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long contact;
    private String emailId;
    private String societyName;

    public Society(SocietyModel updatedSocietyModel){
        this.contact=updatedSocietyModel.getContact();
        this.emailId=updatedSocietyModel.getEmailId();
        this.societyName= updatedSocietyModel.getSocietyName();
    }

    public Society() {

    }
}
