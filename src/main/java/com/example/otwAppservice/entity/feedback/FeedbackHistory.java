package com.example.otwAppservice.entity.feedback;

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
@Table(name = "FeedbackHistory")
public class FeedbackHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long feedbackId;
    private String createdBy; // username
    private Date createdAt;
    private String status;
    private String reason;
    private boolean isActive =  true;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
