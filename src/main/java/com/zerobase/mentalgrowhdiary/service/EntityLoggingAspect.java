package com.zerobase.mentalgrowhdiary.service;

import com.zerobase.mentalgrowhdiary.domain.*;
import com.zerobase.mentalgrowhdiary.repository.FeedbackRequestHistortyRepository;
import com.zerobase.mentalgrowhdiary.repository.MailSendHistoryRepository;
import com.zerobase.mentalgrowhdiary.repository.ReservationHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class EntityLoggingAspect {

    private final FeedbackRequestHistortyRepository feedbackRequestHistortyRepository;
    private final MailSendHistoryRepository mailSendHistoryRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;

    /**
     * 1. Feedback 저장 시 FeedbackRequestHistory 자동 저장
     */
    @AfterReturning(pointcut = "execution(* com.zerobase.mentalgrowhdiary.repository.FeedbackRepository.save(..))", returning = "result")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFeedbackRequestHistory(Object result) {
        if (result instanceof Feedback) {
            Feedback feedback = (Feedback) result;

            FeedbackRequestHistorty history = FeedbackRequestHistorty.builder()
                    .diaryId(feedback.getDiary().getDiaryId())
                    .counselorId(feedback.getCounselor().getCounselorId())
                    .userId(feedback.getDiary().getUser().getUserId())
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            feedbackRequestHistortyRepository.save(history);
        }
    }

    /**
     * 2. Reservation 저장 시 ReservationHistory 자동 저장
     */
    @AfterReturning(pointcut = "execution(* com.zerobase.mentalgrowhdiary.repository.ReservationRepository.save(..))", returning = "result")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logReservationHistory(Object result) {
        if (result instanceof Reservation) {
            Reservation reservation = (Reservation) result;

            ReservationHistory history = ReservationHistory.builder()
                    .reservationId(reservation.getReservationId())
                    .reservationDateTime(reservation.getReservationDateTime())
                    .userId(reservation.getUser().getUserId())
                    .counselorId(reservation.getCounselor().getCounselorId())
                    .status(String.valueOf(reservation.getStatus()))
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            reservationHistoryRepository.save(history);
        }
    }

    /**
     * 3. 메일 전송 시 MailSendHistory 자동 저장
     */
    @AfterReturning(pointcut = "execution(* com.zerobase.mentalgrowhdiary.service.MailService.sendMail(..))", returning = "result")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logMailSendHistory(JoinPoint joinPoint, Object result) {
        if (result instanceof Boolean && (Boolean) result) {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof String) {
                String email = (String) args[0];

                MailSendHistory mailHistory = MailSendHistory.builder()
                        .email(email)
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build();

                mailSendHistoryRepository.save(mailHistory);
            }
        }
    }

}
