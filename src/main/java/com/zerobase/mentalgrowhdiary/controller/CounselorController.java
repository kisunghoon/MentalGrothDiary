package com.zerobase.mentalgrowhdiary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zerobase.mentalgrowhdiary.dto.ClientDiaryResponseDto;
import com.zerobase.mentalgrowhdiary.dto.CounselorRequest;
import com.zerobase.mentalgrowhdiary.dto.CounselorResponseDto;
import com.zerobase.mentalgrowhdiary.service.CounselorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/counselor")
public class CounselorController {

    private final CounselorService counselorService;

    /**
     * 상담사 정보 입력 기능
     * {
     *   "userId": ,
     *   "feature": "SAMPLE특징",
     *   "keywords": ["SAMPLE특징"],
     *   "availableSlots": [
     *     {"day": "Monday", "time": "10:00-12:00"},
     *     {"day": "Wednesday", "time": "14:00-16:00"}
     *   ]
     * }
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
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCounselorInfo(@PathVariable Long id, @RequestBody CounselorRequest request
                                                ,Authentication authentication) throws JsonProcessingException {

        String counselor = authentication.getName();

        request.setUserId(id);
        counselorService.updateInfo(counselor,request);

        return ResponseEntity.ok("상담사 정보 수정이 완료되었습니다.");

    }

    /**
     * 상담사 검색
     * @param name
     * @param feature
     * @param keywords
     * @return
     */
    @PreAuthorize("hasAnyRole('COUNSELOR','CLIENT')")
    @GetMapping("/search")
    public ResponseEntity<List<CounselorResponseDto>> searchCounselors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String feature,
            @RequestParam(required = false) List<String> keywords
    ){
        List<CounselorResponseDto> results = counselorService.searchCounselors(name,feature,keywords);

        return ResponseEntity.ok(results);
    }

    /**
     * 내담자 일기 조회
     */

    @PreAuthorize("hasRole('COUNSELOR')")
    @GetMapping("/clientDiaries")
    public ResponseEntity<List<ClientDiaryResponseDto>> getClientDiaries(Authentication authentication){

        String counselorName = authentication.getName();
        return ResponseEntity.ok(counselorService.getClientDiaries(counselorName));

    }
}
