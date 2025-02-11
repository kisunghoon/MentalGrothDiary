package com.zerobase.mentalgrowhdiary.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
                        containUserNameIgnoreCase(username),
                        eqFeedbackStatus(feedbackStatus),
                        eqReservationStatus(reservationStatus),
                        goeReservationDate(reservationDate)
                )
                .orderBy(qFeedback.createdDate.desc())
                .fetch();

    }

    private BooleanExpression containUserNameIgnoreCase(String username){
        if (username == null || username.isBlank()) {
            return null;
        }
        return QUser.user.username.containsIgnoreCase(username);
    }

    private BooleanExpression eqFeedbackStatus(String feedbackStatus){
        if(feedbackStatus == null || feedbackStatus.isBlank()) {
            return null;
        }

        return QFeedback.feedback.feedbackStatus.eq(FeedbackStatus.valueOf(feedbackStatus));
    }

    private BooleanExpression eqReservationStatus(String reservationStatus){

        if(reservationStatus == null || reservationStatus.isBlank()) {
            return null;
        }

        return QReservation.reservation.status.eq(ReservationStatus.valueOf(reservationStatus));
    }

    private BooleanExpression goeReservationDate(LocalDateTime reservationDate){

        if(reservationDate == null) {
            return null;
        }

        return QReservation.reservation.reservationDateTime.goe(reservationDate);
    }
}
