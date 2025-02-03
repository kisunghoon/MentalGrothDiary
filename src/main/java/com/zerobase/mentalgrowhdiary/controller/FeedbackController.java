package com.zerobase.mentalgrowhdiary.controller;

import com.zerobase.mentalgrowhdiary.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    /**
     * 피드백 요청
     * @param diaryId
     * @param authentication
     * @return
     */
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("{diaryId}/request")
    public ResponseEntity<?> requestFeedback(@PathVariable Long diaryId, Authentication authentication){

        String client = authentication.getName();

        feedbackService.requestFeedback(diaryId,client);

        return ResponseEntity.ok("상담사에게 피드백 요청이 완료되었습니다.");
    }
}
