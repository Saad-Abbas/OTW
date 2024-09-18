package com.example.otwAppservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DeletedUsers")
public class DeletedUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String cardUserName;
    private String cardNumber;
    private String cvc;
    private String expiry;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at", nullable = false, updatable = false)
    private Date deletedAt;

    @PrePersist
    protected void onCreate() {
        deletedAt = new Date();
    }

}
