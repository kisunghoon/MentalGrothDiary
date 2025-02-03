package com.zerobase.mentalgrowhdiary.controller;

import com.zerobase.mentalgrowhdiary.dto.UserCounselorRequest;
import com.zerobase.mentalgrowhdiary.service.UserCounselorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-counselor")
public class UserCounselorController {

    private final UserCounselorService userCounselorService;


    /**
     * 상담사 선정
     */
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/choice")
    public ResponseEntity<?> choiceCounselor(@RequestBody UserCounselorRequest request,
                                             Authentication authentication){

        String username = authentication.getName();

        userCounselorService.choiceCounselor(username,request);

        return ResponseEntity.ok("상담사 선정이 완료되었습니다.");

    }

    /**
     * 상담사 변경
     */
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/change")
    public ResponseEntity<?> changeCounselor(@RequestBody UserCounselorRequest request,
                                             Authentication authentication){

        String username = authentication.getName();

        userCounselorService.changeCounselor(username,request);

        return ResponseEntity.ok("상담사 변경이 완료되었습니다.");
    }



}
