package com.zerobase.mentalgrowhdiary.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.mentalgrowhdiary.domain.*;
import com.zerobase.mentalgrowhdiary.dto.FeedbackWithLatestReservationDTO;
import com.zerobase.mentalgrowhdiary.repository.custom.FeedbackRepositoryCustom;
import com.zerobase.mentalgrowhdiary.type.FeedbackStatus;
import com.zerobase.mentalgrowhdiary.type.ReservationStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class FeedbackRepositoryImpl implements FeedbackRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FeedbackWithLatestReservationDTO> getFeedbackWithLatestReservation(String username, String feedbackStatus, String reservationStatus,
                                                                                   LocalDateTime reservationDate) {

        QFeedback qFeedback = QFeedback.feedback;
        QDiary qDiary = QDiary.diary;
        QReservation qReservation = QReservation.reservation;
        QUser qUser = QUser.user;

        QReservation r2 = new QReservation("r2");


        return jpaQueryFactory
                .select(Projections.constructor(
                        FeedbackWithLatestReservationDTO.class,
                        qFeedback.feedbackId,
                        qFeedback.feedbackStatus.stringValue(),
                        qFeedback.createdDate,
                        qDiary.diaryId,
                        qDiary.updatedDate,
                        qReservation.reservationId,
                        qReservation.reservationDateTime,
                        qReservation.status.stringValue()
                ))
                .from(qFeedback)
                .join(qDiary).on(qFeedback.diary.eq(qDiary))
                .join(qUser).on(qDiary.user.eq(qUser))
                .leftJoin(qReservation).on(qReservation.user.eq(qUser)
                        .and(qReservation.reservationDateTime.eq(
                                JPAExpressions.select(r2.reservationDateTime.max())
                                        .from(r2)
                                        .where(r2.user.eq(qUser))
                        )))
                .where(
                        username != null ? qUser.username.containsIgnoreCase(username) : null,
                        feedbackStatus != null ? qFeedback.feedbackStatus.eq(FeedbackStatus.valueOf(feedbackStatus)) : null,
                        reservationStatus != null ? qReservation.status.eq(ReservationStatus.valueOf(reservationStatus)) : null,
                        reservationDate != null ? qReservation.reservationDateTime.goe(reservationDate) : null
                )
                .orderBy(qFeedback.createdDate.desc())
                .fetch();

    }
}
