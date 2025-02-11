package com.zerobase.mentalgrowhdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackWithLatestReservationDTO {

    private Long feedbackId;
    private String feedbackStatus;
    private LocalDateTime feedbackDate;
    private Long diaryId;
    private LocalDateTime diaryDate;
    private Long reservationId;
    private LocalDateTime reservationDateTime;
    private String reservationStatus;

}
