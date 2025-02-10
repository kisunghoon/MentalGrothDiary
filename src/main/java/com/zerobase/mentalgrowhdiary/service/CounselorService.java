package com.zerobase.mentalgrowhdiary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.mentalgrowhdiary.domain.Counselor;
import com.zerobase.mentalgrowhdiary.domain.Diary;
import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.dto.ClientDiaryResponseDto;
import com.zerobase.mentalgrowhdiary.dto.CounselorRequest;
import com.zerobase.mentalgrowhdiary.dto.CounselorResponseDto;
import com.zerobase.mentalgrowhdiary.exception.MentalGrowthException;
import com.zerobase.mentalgrowhdiary.repository.*;
import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final UserCounselorRepository userCounselorRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void registerInfo(CounselorRequest request, String counselorName) throws JsonProcessingException {

        User user = userRepository.findByUsername(counselorName)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        Counselor counselor = Counselor.builder()
                .user(user)
                .feature(request.getFeature())
                .keywords(objectMapper.writeValueAsString(request.getKeywords()))
                .availableSlots(objectMapper.writeValueAsString(request.getAvailableSlots()))
                .build();

        counselorRepository.save(counselor);
    }

    @Transactional
    public void updateInfo(String counselorName,CounselorRequest request) throws JsonProcessingException {

        User user = userRepository.findByUsername(counselorName)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        Counselor counselor = counselorRepository.findByUser(user)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.COUNSELOR_NOT_FOUND));


        counselor.setFeature(request.getFeature());
        counselor.setKeywords(objectMapper.writeValueAsString(request.getKeywords()));
        counselor.setAvailableSlots(objectMapper.writeValueAsString(request.getAvailableSlots()));

        counselorRepository.save(counselor);
    }

    @Transactional(readOnly = true)
    public List<CounselorResponseDto> searchCounselors(String name, String feature, List<String> keywords) {

        List<Counselor> counselors = counselorRepository.searchCounselors(name, feature, keywords);

        return counselors.stream().map(
                counselor -> CounselorResponseDto.fromEntity(counselor,objectMapper)).toList();
    }

    @Transactional(readOnly = true)
    public List<ClientDiaryResponseDto> getClientDiaries(String counselorName) {


        List<Diary> diaries = userCounselorRepository.findClientDiaryByConditions(counselorName);

        return diaries.stream()
                .map(diary -> {
                    return new ClientDiaryResponseDto(
                            diary.getUser().getUserId(),
                            diary.getUser().getUsername(),
                            diary.getDiaryId(),
                            diary.getDiaryContent(),
                            diary.getDiaryDate()
                    );
                })
                .toList();
    }

}