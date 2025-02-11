package com.zerobase.mentalgrowhdiary.service;

import com.zerobase.mentalgrowhdiary.domain.*;
import com.zerobase.mentalgrowhdiary.dto.FeedbackRequest;
import com.zerobase.mentalgrowhdiary.dto.FeedbackResponse;
import com.zerobase.mentalgrowhdiary.dto.FeedbackWithLatestReservationDTO;
import com.zerobase.mentalgrowhdiary.exception.MentalGrowthException;
import com.zerobase.mentalgrowhdiary.repository.*;
import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import com.zerobase.mentalgrowhdiary.type.FeedbackStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final UserCounselorRepository userCounselorRepository;
    private final CounselorRepository counselorRepository;
    private final MailService mailService;


    @Transactional
    public void requestFeedback(Long diaryId, String client) {

        User user = userRepository.findByUsername(client)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.DIARY_NOT_FOUND));

        if(!diary.getUser().equals(user)) {
            throw new MentalGrowthException(ErrorCode.FORBIDDEN);
        }


        if(feedbackRepository.findByDiary(diary).isPresent()) {
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

    @Transactional
    public void registerFeedback(Long diaryId, String counselorName, FeedbackRequest request) {

        Counselor counselor = counselorRepository.findByUser_Username(counselorName)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.DIARY_NOT_FOUND));

        UserCounselor userCounselor = userCounselorRepository.findByUserAndActive(diary.getUser(),true)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.NO_ACTIVE_COUNSELOR));

        Feedback feedback = feedbackRepository.findById(request.getFeedbackId())
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.FEEDBACK_NOT_FOUND));

        feedback.setFeedbackContent(request.getFeedbackContent());
        feedback.setFeedbackStatus(FeedbackStatus.COMPLETED);

        feedbackRepository.save(feedback);

        String subject = "피드백 완료 알림";
        String body = mailService.getFeedbackCompletedTemplete(diary.getUser().getUsername());

        boolean isMailSent = mailService.sendMail(diary.getUser().getEmail(), subject, body);

        if(!isMailSent) {
            throw new MentalGrowthException(ErrorCode.MAIL_SEND_FAIL);
        }

    }

    @Transactional(readOnly = true)
    public Page<FeedbackResponse> getFeedback(String client, Pageable pageable) {

        User user = userRepository.findByUsername(client)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));


        return feedbackRepository.findByDiaryUser(user,pageable)
                .map(feedback -> new FeedbackResponse(
                        feedback.getFeedbackId(),
                        feedback.getDiary().getDiaryId(),
                        feedback.getCounselor().getCounselorId(),
                        feedback.getFeedbackContent(),
                        feedback.getCreatedDate(),
                        feedback.getUpdatedDate()
                ));
    }


    @Transactional
    public void updateFeedback(Long feedbackId, String counselorName , FeedbackRequest request) {

        Counselor counselor = counselorRepository.findByUser_Username(counselorName)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.FEEDBACK_NOT_FOUND));


        if(!feedback.getCounselor().equals(counselor)){
            throw new MentalGrowthException(ErrorCode.FORBIDDEN);
        }

        feedback.setFeedbackContent(request.getFeedbackContent());

        feedbackRepository.save(feedback);
    }

    @Transactional(readOnly = true)
    public List<FeedbackWithLatestReservationDTO> getFeedbackWithLatestReservations(String username,
                                                                                    String feedbackStatus, String reservationStatus, LocalDateTime reservationDate) {

        return feedbackRepository.getFeedbackWithLatestReservation(username,feedbackStatus,reservationStatus,reservationDate);
    }

    /**
     * 2일 이상 피드백이 작성되지 않은 경우 , 상담사에게 이메일 발송
     */
    @Transactional
    public void sendFeedbackReminder(){
        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);

        List<Feedback> pendingFeedback  = feedbackRepository.findPendingFeedbacks(twoDaysAgo);

        if(pendingFeedback.isEmpty()){
            return;
        }

        Map<Counselor, List<Feedback>> feedbackGroupByCounselor = pendingFeedback.stream().
                collect(Collectors.groupingBy(Feedback::getCounselor));


        for(Map.Entry<Counselor, List<Feedback>> entry : feedbackGroupByCounselor.entrySet()){
            sendFeedbackReminder(entry.getKey(),entry.getValue());
        }

    }

    private void sendFeedbackReminder(Counselor counselor, List<Feedback> feedbackList) {
        String counselorName = counselor.getUser().getUsername();
        String counselorEmail = counselor.getUser().getEmail();

        StringBuilder sb = new StringBuilder();

        for(Feedback feedback : feedbackList){
            String clientName = feedback.getDiary().getUser().getUsername();
            Long diaryId = feedback.getDiary().getDiaryId();
            sb.append(String.format("클라이언트: %s, 일기 ID: %d%n", clientName, diaryId));
        }

        String subject = "피드백 작성 촉구 안내";
        String body = mailService.getFeedbackReminderTemplate(counselorName,sb.toString());

        boolean isMailSent = mailService.sendMail(counselorEmail, subject, body);

        if(!isMailSent) {
            throw new MentalGrowthException(ErrorCode.MAIL_SEND_FAIL);
        }
    }

  /*  public void sendFeedbackReminder(Feedback feedback){

        String counselorName = feedback.getCounselor().getUser().getUsername();
        String clientName = feedback.getDiary().getUser().getUsername();
        Long diaryId = feedback.getDiary().getDiaryId();
        String counselorEmail = feedback.getCounselor().getUser().getEmail();

        String subject = "피드백 작성 촉구 안내";
        String body = mailService.getFeedbackReminderTemplate(counselorName,clientName,diaryId);

        boolean isMailSent = mailService.sendMail(counselorEmail, subject, body);

        if(!isMailSent) {
            throw new MentalGrowthException(ErrorCode.MAIL_SEND_FAIL);
        }
    }*/
}
