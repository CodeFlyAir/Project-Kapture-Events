package com.kaptureevents.KaptureEvents.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "society_auth")
@AllArgsConstructor
@NoArgsConstructor
public class SocietyAuth {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private  Long id;

   @Column(nullable = false,unique = true)
   private  String email;
   @Column(nullable = false)
   private String password;

   public SocietyAuth(String email, String password) {
      this.email = email;
      this.password = password;
   }
}
