package com.zerobase.mentalgrowhdiary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.mentalgrowhdiary.domain.Diary;
import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.dto.DiaryRequest;
import com.zerobase.mentalgrowhdiary.dto.DiaryResponseDto;
import com.zerobase.mentalgrowhdiary.exception.MentalGrowthException;
import com.zerobase.mentalgrowhdiary.repository.DiaryRepository;
import com.zerobase.mentalgrowhdiary.repository.FeedbackRepository;
import com.zerobase.mentalgrowhdiary.repository.UserCounselorRepository;
import com.zerobase.mentalgrowhdiary.repository.UserRepository;
import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final UserCounselorRepository userCounselorRepository;
    private final FeedbackRepository feedbackRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void registerDiary(DiaryRequest request, String client) throws JsonProcessingException {


        User user = userRepository.findByUsername(client)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        if(diaryRepository.existsByUserAndDiaryDate(user,request.getDiaryDate())){
            throw new MentalGrowthException(ErrorCode.DIARY_ALERADY_REGISTER);
        }

        if(!userCounselorRepository.existsByUserAndActive(user,true)){
            throw new MentalGrowthException(ErrorCode.NOT_YET_SELECTED_COUNSELOR);
        }

        Diary diary = Diary.builder()
                .user(user)
                .diaryDate(request.getDiaryDate())
                .diaryContent(objectMapper.writeValueAsString(request.getDiaryContent()))
                .hashtag(objectMapper.writeValueAsString(request.getHashtag()))
                .build();

        diaryRepository.save(diary);
    }

    @Transactional
    public void updateDiary(String client,Long diaryId,DiaryRequest request) throws JsonProcessingException {

        User user = userRepository.findByUsername(client)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.DIARY_NOT_FOUND));

        if(!diary.getUser().equals(user)){
            throw new MentalGrowthException(ErrorCode.FORBIDDEN);
        }

        diary.setDiaryDate(request.getDiaryDate());
        diary.setDiaryContent(objectMapper.writeValueAsString(request.getDiaryContent()));
        diary.setHashtag(objectMapper.writeValueAsString(request.getHashtag()));

        diaryRepository.save(diary);

    }

    @Transactional
    public void deleteDiary(Long diaryId , String client) {

        User user = userRepository.findByUsername(client)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.DIARY_NOT_FOUND));

        if(!diary.getUser().equals(user)){
            throw new MentalGrowthException(ErrorCode.FORBIDDEN);
        }

        if(feedbackRepository.existsByDiary(diary)){
            throw new MentalGrowthException(ErrorCode.DIARY_HAS_FEEDBACK);
        }

        diaryRepository.delete(diary);

    }

    @Transactional(readOnly = true)
    public List<DiaryResponseDto> searchDiary(String client, LocalDate diaryDate, String hashtag) {
        User user = userRepository.findByUsername(client)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        List<Diary> diaries = diaryRepository.searchDiaries(user.getUserId(),diaryDate,hashtag);

        return diaries.stream().map(DiaryResponseDto::fromEntity).toList();
    }

}
