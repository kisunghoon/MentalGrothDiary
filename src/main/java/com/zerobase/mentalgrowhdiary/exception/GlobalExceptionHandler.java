package com.zerobase.mentalgrowhdiary.exception;

import com.zerobase.mentalgrowhdiary.dto.ErrorResponse;
import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MentalGrowthException.class)
    public ErrorResponse handleMentalGrowthException(MentalGrowthException e) {
        log.error("handleMentalGrowthException is occured ", e.getMessage());

        return new ErrorResponse(e.getErrorCode(),e.getErrorMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException is occured ", e);

        return new ErrorResponse(ErrorCode.FORBIDDEN , "접근 권한이 없습니다.");
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneralException(Exception e) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);

        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다.");
    }
}
