package com.zerobase.mentalgrowhdiary.repository;

import com.zerobase.mentalgrowhdiary.domain.Diary;
import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.repository.custom.DiaryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long>, DiaryRepositoryCustom {

    boolean existsByUserAndDiaryDate(User user, LocalDate diaryDate);

    List<Diary> findByUser(User user);

    //일기 작성일 기준 7일
    List<Diary> findTop7ByUserOrderByDiaryDateDesc(User user);
}
