package com.example.otwAppservice.service.feedbackService;

import com.example.otwAppservice.entity.feedback.Feedback;
import com.example.otwAppservice.entity.feedback.FeedbackHistory;

import java.util.List;

public interface FeedbackService {

    public List<Feedback> getAllFeedbackByUserId(Long userId);

    Feedback saveFeedback(Feedback feedback);

    FeedbackHistory updateFeedbackStatus(FeedbackHistory feedbackHistory);

}
