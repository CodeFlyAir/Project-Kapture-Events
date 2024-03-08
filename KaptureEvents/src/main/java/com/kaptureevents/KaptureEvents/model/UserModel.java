package com.kaptureevents.KaptureEvents.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserModel {
    String email;
    String name;
    String picture;
    boolean verified = false;

    public static UserModel getInstance() {
        return UserModelHolder.INSTANCE;
    }

    private static class UserModelHolder {
        private static final UserModel INSTANCE = new UserModel();
    }
}
