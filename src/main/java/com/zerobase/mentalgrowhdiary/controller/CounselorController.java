package com.zerobase.mentalgrowhdiary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zerobase.mentalgrowhdiary.dto.CounselorRequest;
import com.zerobase.mentalgrowhdiary.service.CounselorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/counselor")
public class CounselorController {

    private final CounselorService counselorService;

    /**
     * 상담사 정보 입력 기능
     * */
    @PreAuthorize("hasRole('COUNSELOR')")
    @PostMapping("/register")
    public ResponseEntity<?> registerCounselorInfo(@RequestBody CounselorRequest request,
                                                Authentication authentication) throws JsonProcessingException {

        String counselor = authentication.getName();


        counselorService.registerInfo(request,counselor);

        return ResponseEntity.ok("상담사 정보 입력이 완료 되었습니다.");

    }

    /**
     * 상담사 정보 수정 기능
     * */
    @PreAuthorize("hasRole('COUNSELOR')")
    @PutMapping("/update")
    public ResponseEntity<?> updateCounselorInfo(@RequestBody CounselorRequest request ,
                                                 Authentication authentication) throws JsonProcessingException {


        counselorService.updateInfo(request);

        return ResponseEntity.ok("상담사 정보 수정이 완료되었습니다.");


    }

}
