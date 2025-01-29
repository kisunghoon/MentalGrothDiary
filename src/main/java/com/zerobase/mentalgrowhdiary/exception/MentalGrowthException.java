package com.zerobase.mentalgrowhdiary.exception;


import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentalGrowthException extends RuntimeException{

    private ErrorCode errorCode;
    private String errorMessage;

    public MentalGrowthException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDesc();
    }
}
