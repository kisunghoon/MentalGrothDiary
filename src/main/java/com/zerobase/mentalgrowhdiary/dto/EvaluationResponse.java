package com.zerobase.mentalgrowhdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResponse {
    private Long evaluationId;
    private String diaryContent;
    private Long counselorId;
    private String assignment;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
