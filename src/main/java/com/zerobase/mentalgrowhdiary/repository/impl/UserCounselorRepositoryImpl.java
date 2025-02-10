package com.zerobase.mentalgrowhdiary.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.mentalgrowhdiary.domain.*;
import com.zerobase.mentalgrowhdiary.repository.custom.UserCounselorRepositoryCustom;
import lombok.RequiredArgsConstructor;


import java.util.List;

@RequiredArgsConstructor
public class UserCounselorRepositoryImpl implements UserCounselorRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 상담사에게 피드백 요청된 다이어리 조회
    @Override
    public List<Diary> findClientDiaryByConditions(String counselorName) {

        QDiary diary = QDiary.diary;
        QFeedback feedback = QFeedback.feedback;

        List<Diary> feedbackClient = queryFactory.select(feedback.diary)
                .from(feedback)
                .where(feedback.counselor.user.username.eq(counselorName))
                .fetch();


        return feedbackClient;
    }
}
