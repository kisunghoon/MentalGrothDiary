package com.zerobase.mentalgrowhdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationRequest {

    private Long diaryId;
    private Long counselorId;
    private String assessment;
}
