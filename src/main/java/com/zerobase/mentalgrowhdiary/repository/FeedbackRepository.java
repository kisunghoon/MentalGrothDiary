package com.zerobase.mentalgrowhdiary.repository;

import com.zerobase.mentalgrowhdiary.domain.Diary;
import com.zerobase.mentalgrowhdiary.domain.Feedback;
import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.repository.custom.FeedbackRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {

    boolean existsByDiary(Diary diary);
    Optional<Feedback> findByDiary(Diary diary);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM feedback f " +
            "JOIN diary d ON f.diary_id = d.diary_id " +
            "WHERE d.user_id = :userId AND f.feedback_status = 'PROGRESS')",
            nativeQuery = true)
    Integer hasProgessFeedback(@Param("userId") Long userId);

    Page<Feedback> findByDiaryUser(User user, Pageable pageable);

    @Query(value = "SELECT f.* FROM feedback f " +
            "JOIN diary d ON f.diary_id = d.diary_id " +
            "WHERE d.created_date <= :twoDaysAgo " +
            "AND f.feedback_status = 'PROGRESS'", nativeQuery = true)
    List<Feedback> findPendingFeedbacks(@Param("twoDaysAgo") LocalDateTime twoDaysAgo);

}
