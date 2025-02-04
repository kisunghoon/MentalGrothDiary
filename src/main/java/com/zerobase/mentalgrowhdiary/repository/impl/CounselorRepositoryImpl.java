package com.zerobase.mentalgrowhdiary.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.mentalgrowhdiary.domain.Counselor;
import com.zerobase.mentalgrowhdiary.domain.QCounselor;
import com.zerobase.mentalgrowhdiary.repository.custom.CounselorRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CounselorRepositoryImpl implements CounselorRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Counselor> searchCounselors(String username, String feature, List<String> keywords) {

        QCounselor counselor = QCounselor.counselor;
        BooleanBuilder builder = new BooleanBuilder();

        if(username != null && !(username.isEmpty())) {
            builder.or(counselor.user.username.containsIgnoreCase(username));
        }

        if(feature != null && !(feature.isEmpty())) {
            builder.or(counselor.feature.containsIgnoreCase(feature));
        }

        if(keywords != null && !(keywords.isEmpty())) {
            for(String keyword : keywords) {
                builder.or(counselor.keywords.containsIgnoreCase(keyword));
            }
        }

        return queryFactory.selectFrom(counselor).where(builder).fetch();
    }
}