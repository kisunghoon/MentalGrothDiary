package com.zerobase.mentalgrowhdiary.controller;

import com.zerobase.mentalgrowhdiary.dto.FeedbackRequest;
import com.zerobase.mentalgrowhdiary.dto.FeedbackResponse;
import com.zerobase.mentalgrowhdiary.dto.FeedbackWithLatestReservationDTO;
import com.zerobase.mentalgrowhdiary.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    //피드백 조회 (자신의 상담사에 피드백 조회)
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping
    public ResponseEntity<?> getFeedback(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size , Authentication authentication){

        String client = authentication.getName();
        Pageable pageable = PageRequest.of(page,size, Sort.by("updatedDate").descending());

        Page<FeedbackResponse> feedbacks = feedbackService.getFeedback(client,pageable);

        return ResponseEntity.ok(feedbacks);
    }

    //피드백 작성
    @PreAuthorize("hasRole('COUNSELOR')")
    @PutMapping("/{diaryId}/register")
    public ResponseEntity<?> registerFeedback(@PathVariable Long diaryId, @RequestBody FeedbackRequest request ,Authentication authentication){

        String counselor = authentication.getName();

        feedbackService.registerFeedback(diaryId,counselor,request);

        return ResponseEntity.ok("피드백 작성이 완료되었습니다.");
    }

    //피드백 수정
    @PreAuthorize("hasRole('COUNSELOR')")
    @PutMapping("/{diaryId}/update")
    public ResponseEntity<?> updateFeedback(@PathVariable Long diaryId, @RequestBody FeedbackRequest request ,Authentication authentication){

        String counselor = authentication.getName();

        feedbackService.updateFeedback(request.getFeedbackId(),counselor,request);

        return ResponseEntity.ok("피드백 수정이 완료 되었습니다.");
    }

    /**
     * 내담자 피드백 작성 상태 및 최신 예약 현황 확인
     */
    @PreAuthorize("hasRole('COUNSELOR')")
    @GetMapping("/clientManageSearch")
    public ResponseEntity<List<FeedbackWithLatestReservationDTO>> getFeedbackWithLatestReservation(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String feedbackStatus,
            @RequestParam(required = false) String reservationStatus,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime reservationDate) {

        List<FeedbackWithLatestReservationDTO> feedbacks = feedbackService.getFeedbackWithLatestReservations(
                username, feedbackStatus, reservationStatus, reservationDate);

        return ResponseEntity.ok(feedbacks);

    }


}
