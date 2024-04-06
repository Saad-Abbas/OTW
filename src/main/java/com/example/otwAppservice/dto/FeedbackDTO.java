package com.example.otwAppservice.dto;

import com.example.otwAppservice.entity.User;
import com.example.otwAppservice.entity.UserCardDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {

    private long userId;

    private String feedbackCode;
    private String feedbackText;
    private String feedbackType;
    private String feedbackRecording;
    private String feedbackStatus;
    private Date feedbackTime;
    private Date updatedAt;
    private boolean isActive;
}
