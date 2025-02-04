package com.zerobase.mentalgrowhdiary.repository;

import com.zerobase.mentalgrowhdiary.domain.Diary;
import com.zerobase.mentalgrowhdiary.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByDiary(Diary diary);
    Optional<Feedback> findByDiary(Diary diary);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM feedback f " +
            "JOIN diary d ON f.diary_id = d.diary_id " +
            "WHERE d.user_id = :userId AND f.feedback_status = 'PROGRESS')",
            nativeQuery = true)
    Integer hasProgessFeedback(@Param("userId") Long userId);
}
