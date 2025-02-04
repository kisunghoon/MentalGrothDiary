package com.zerobase.mentalgrowhdiary.repository.custom;

import com.zerobase.mentalgrowhdiary.domain.Diary;

import java.time.LocalDate;
import java.util.List;

public interface DiaryRepositoryCustom {

    List<Diary> searchDiaries(Long userId, LocalDate diaryDate, String hashtag);
}
