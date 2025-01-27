package com.zerobase.mentalgrowhdiary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.mentalgrowhdiary.domain.Counselor;
import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.dto.CounselorRequest;
import com.zerobase.mentalgrowhdiary.exception.MentalGrowthException;
import com.zerobase.mentalgrowhdiary.repository.CounselorRepository;
import com.zerobase.mentalgrowhdiary.repository.UserRepository;
import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CounselorService {

    private final CounselorRepository counselorRepository;
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
    public void updateInfo(CounselorRequest request) throws JsonProcessingException {


        Counselor counselor = counselorRepository.findByUser_UserId(request.getUserId())
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.COUNSELOR_NOT_FOUND));


        counselor.setFeature(request.getFeature());
        counselor.setKeywords(objectMapper.writeValueAsString(request.getKeywords()));
        counselor.setAvailableSlots(objectMapper.writeValueAsString(request.getKeywords()));

        counselorRepository.save(counselor);

    }
}
