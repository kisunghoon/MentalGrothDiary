package com.zerobase.mentalgrowhdiary.dto;

import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}
