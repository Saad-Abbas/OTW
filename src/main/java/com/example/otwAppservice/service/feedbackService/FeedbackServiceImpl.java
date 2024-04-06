package com.example.otwAppservice.service.feedbackService;


import com.example.otwAppservice.entity.feedback.Feedback;
import com.example.otwAppservice.entity.feedback.FeedbackHistory;
import com.example.otwAppservice.repository.FeedbackHistoryRepository;
import com.example.otwAppservice.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {


    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    FeedbackHistoryRepository feedbackHistoryRepository;

    @Override
    public List<Feedback> getAllFeedbackByUserId(Long userId) {
        return feedbackRepository.findAllByUserIdOrderByIdDesc(userId);
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public FeedbackHistory updateFeedbackStatus(FeedbackHistory feedbackHistory) {
        return feedbackHistoryRepository.save(feedbackHistory);
    }
}
