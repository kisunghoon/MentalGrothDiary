package com.zerobase.mentalgrowhdiary.repository.custom;

import com.zerobase.mentalgrowhdiary.domain.Counselor;

import java.util.List;

public interface CounselorRepositoryCustom {

    List<Counselor> searchCounselors(String username, String feature, List<String> keywords);
}
