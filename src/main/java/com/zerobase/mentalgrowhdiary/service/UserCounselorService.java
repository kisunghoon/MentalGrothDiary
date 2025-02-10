package com.zerobase.mentalgrowhdiary.service;

import com.zerobase.mentalgrowhdiary.domain.Counselor;
import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.domain.UserCounselor;
import com.zerobase.mentalgrowhdiary.dto.UserCounselorRequest;
import com.zerobase.mentalgrowhdiary.exception.MentalGrowthException;
import com.zerobase.mentalgrowhdiary.repository.CounselorRepository;
import com.zerobase.mentalgrowhdiary.repository.FeedbackRepository;
import com.zerobase.mentalgrowhdiary.repository.UserCounselorRepository;
import com.zerobase.mentalgrowhdiary.repository.UserRepository;
import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCounselorService {

    private final UserCounselorRepository userCounselorRepository;
    private final CounselorRepository counselorRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public void choiceCounselor(String username, UserCounselorRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        Counselor counselor = counselorRepository.findById(request.getCounselorId())
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.COUNSELOR_NOT_FOUND));

        if(userCounselorRepository.existsByUserAndCounselor(user, counselor)){
            throw new MentalGrowthException(ErrorCode.ALERADY_SELECTED_COUNSELOR);
        }

        if(userCounselorRepository.existsByUserAndActive(user, true)){
            throw new MentalGrowthException(ErrorCode.MAX_SELECTED_COUNSELOR);
        }

        UserCounselor userCounselor = UserCounselor
                .builder()
                .user(user)
                .counselor(counselor)
                .active(true)
                .build();

        userCounselorRepository.save(userCounselor);
    }

    @Transactional
    public void changeCounselor(String username, UserCounselorRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        Counselor counselor = counselorRepository.findById(request.getCounselorId())
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.COUNSELOR_NOT_FOUND));

        Integer hasProgessFeedback = feedbackRepository.hasProgessFeedback(user.getUserId());
        log.info("hasProgessFeedback : {}",hasProgessFeedback);

        if(hasProgessFeedback >= 1){
            throw new MentalGrowthException(ErrorCode.FEEDBACK_PROGESS);
        }


        userCounselorRepository.findByUserAndActive(user, true)
                .ifPresent(userCounselor ->{
                    userCounselor.setActive(false);
                });

        UserCounselor newUserCounselor = UserCounselor
                                                    .builder()
                                                    .user(user)
                                                    .counselor(counselor)
                                                    .active(true)
                                                    .build();

        userCounselorRepository.save(newUserCounselor);

    }
}
