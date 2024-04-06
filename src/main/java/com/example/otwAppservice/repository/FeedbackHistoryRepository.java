package com.example.otwAppservice.repository;

import com.example.otwAppservice.entity.feedback.FeedbackHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackHistoryRepository extends JpaRepository<FeedbackHistory,Long> {
}
