package com.example.otwAppservice.entity.feedback;


import com.example.otwAppservice.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "Feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private User user;

    private String feedbackCode;
    private String feedbackText;
    private String feedbackRecording;
    private String feedbackStatus;
    private String feedbackType;
    private Date feedbackTime;
    private Date updatedAt;
    private boolean isActive;


    @PrePersist
    protected void onCreate() {
        feedbackTime = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }


}
