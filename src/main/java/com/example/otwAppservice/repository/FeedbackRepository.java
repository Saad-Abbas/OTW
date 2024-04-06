package com.example.otwAppservice.repository;

import com.example.otwAppservice.entity.feedback.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByUserIdOrderByIdDesc(Long userId);
}
