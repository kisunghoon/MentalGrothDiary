package com.zerobase.mentalgrowhdiary.service;

import com.zerobase.mentalgrowhdiary.domain.Diary;
import com.zerobase.mentalgrowhdiary.domain.Feedback;
import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.domain.UserCounselor;
import com.zerobase.mentalgrowhdiary.exception.MentalGrowthException;
import com.zerobase.mentalgrowhdiary.repository.DiaryRepository;
import com.zerobase.mentalgrowhdiary.repository.FeedbackRepository;
import com.zerobase.mentalgrowhdiary.repository.UserCounselorRepository;
import com.zerobase.mentalgrowhdiary.repository.UserRepository;
import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import com.zerobase.mentalgrowhdiary.type.FeedbackStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final UserCounselorRepository userCounselorRepository;


    @Transactional
    public void requestFeedback(Long diaryId, String client) {

        User user = userRepository.findByUsername(client)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.DIARY_NOT_FOUND));

        if(!diary.getUser().equals(user)) {
            throw new MentalGrowthException(ErrorCode.FORBIDDEN);
        }

        Optional<Feedback> existFeedback = feedbackRepository.findByDiary(diary);

        if(existFeedback.isPresent()) {
            throw new MentalGrowthException(ErrorCode.FEEDBACK_ALREADY_REQUEST);
        }

        UserCounselor userCounselor = userCounselorRepository.findByUserAndActive(user,true)
                .orElseThrow(()-> new MentalGrowthException(ErrorCode.NO_ACTIVE_COUNSELOR));



        Feedback feedback = Feedback.builder()
                                    .diary(diary)
                                    .counselor(userCounselor.getCounselor())
                                    .feedbackStatus(FeedbackStatus.PROGRESS)
                                    .build();

        feedbackRepository.save(feedback);
    }
}
