package com.zerobase.mentalgrowhdiary.repository.custom;

import com.zerobase.mentalgrowhdiary.domain.Diary;

import java.util.List;

public interface UserCounselorRepositoryCustom {

    List<Diary> findClientDiaryByCounselorName(String counselorName);
}
