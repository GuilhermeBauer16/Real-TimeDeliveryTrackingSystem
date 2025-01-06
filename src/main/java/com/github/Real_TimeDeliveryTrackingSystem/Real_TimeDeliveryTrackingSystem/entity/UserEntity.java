package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;

    @Column(name = "user_profile")
    @Enumerated(EnumType.STRING)
    private UserProfile userProfile;

    @Column(name = "verify_code")
    private String verifyCode;

    private boolean authenticated;

    @Column(name = "code_expiration")
    private LocalDateTime codeExpiration;

    public UserEntity(String id, String name, String email, String password, UserProfile userProfile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userProfile = userProfile;
    }
}
