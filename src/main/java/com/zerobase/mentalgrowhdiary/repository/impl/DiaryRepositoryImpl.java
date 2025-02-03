package com.zerobase.mentalgrowhdiary.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.mentalgrowhdiary.domain.Diary;
import com.zerobase.mentalgrowhdiary.domain.QDiary;
import com.zerobase.mentalgrowhdiary.repository.custom.DiaryRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryImpl implements DiaryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Diary> searchDiaries(Long userId, LocalDate diaryDate, String hashtag) {


        QDiary diary = QDiary.diary;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(diary.user.userId.eq(userId));

        BooleanBuilder optional = new BooleanBuilder();

        if(diaryDate != null) {
            optional.or(diary.diaryDate.eq(diaryDate));
        }

        if(hashtag != null && !hashtag.isEmpty()) {
            optional.or(diary.hashtag.containsIgnoreCase(hashtag));

        }

        builder.and(optional);
        return queryFactory.selectFrom(diary)
                            .where(builder)
                            .orderBy(diary.diaryDate.desc())
                            .fetch();
    }
}
