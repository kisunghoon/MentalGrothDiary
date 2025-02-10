package com.zerobase.mentalgrowhdiary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    private final FeedbackService feedbackService;
    private final ReservationService reservationService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendFeedbackReminders(){

        log.info("sendFeedbackReminders 스케줄러 시작 ");

        feedbackService.sendFeedbackReminder();
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendOfflineCounselingReminders(){

        log.info("sendOfflineCounselingReminders 스케줄러 시작");

        reservationService.sendDailyCounselingReminders();
    }
}
