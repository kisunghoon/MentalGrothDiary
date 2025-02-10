package com.zerobase.mentalgrowhdiary.repository.custom;

import com.zerobase.mentalgrowhdiary.domain.Feedback;
import com.zerobase.mentalgrowhdiary.dto.FeedbackWithLatestReservationDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackRepositoryCustom {

    List<FeedbackWithLatestReservationDTO> getFeedbackWithLatestReservation(String username, String feedbackStatus, String reservationStatus, LocalDateTime reservationDate);
}
