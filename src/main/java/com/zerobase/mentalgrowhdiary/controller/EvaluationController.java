package com.zerobase.mentalgrowhdiary.controller;

import com.zerobase.mentalgrowhdiary.dto.EvaluationRequest;
import com.zerobase.mentalgrowhdiary.dto.EvaluationResponse;
import com.zerobase.mentalgrowhdiary.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    /**
     * 내담자 심리 평가 작성
     * @param request
     * @param authentication
     * @return
     */
    @PreAuthorize("hasRole('COUNSELOR')")
    @PostMapping("/register")
    public ResponseEntity<?> registerEvaluation(@RequestBody EvaluationRequest request,
                                                Authentication authentication) {


        String counselor = authentication.getName();

        evaluationService.registerEvaluation(request,counselor);

        return ResponseEntity.ok("내담자의 심리적 평가 작성이 완료되었습니다. ");
    }

    /**
     * 내담자 심리 평가 수정
     * @param id
     * @param request
     * @param authentication
     * @return
     */
    @PreAuthorize("hasRole('COUNSELOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvaluation(
            @PathVariable Long id, @RequestBody EvaluationRequest request,
            Authentication authentication){

        String counselor = authentication.getName();

        evaluationService.updateEvaluation(id,request,counselor);

        return ResponseEntity.ok("내담자의 심리적 평가가 수정되었습니다.");
    }

    /**
     * 내담자 심리 평가 삭제
     * @param id
     * @param authentication
     * @return
     */
    @PreAuthorize("hasRole('COUNSELOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvaluation(@PathVariable Long id , Authentication authentication) {

        String counselor = authentication.getName();

        evaluationService.deleteEvaluation(id,counselor);

        return ResponseEntity.ok("내담의 심리적 평가 삭제가 완료되었습니다.");
    }

    /**
     * 내담자 심리 평가 및 일기 조회
     * @param page
     * @param size
     * @param authentication
     * @return
     */
    @PreAuthorize("hasRole('COUNSELOR')")
    @GetMapping
    public ResponseEntity<?> getEvaluations(@RequestParam(defaultValue = "0") int page ,
                                            @RequestParam(defaultValue = "10") int size,Authentication authentication){

        String counselorName = authentication.getName();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").ascending());

        Page<EvaluationResponse> evaluations = evaluationService.getEvaluations(counselorName,pageable);

        return ResponseEntity.ok(evaluations);
    }



}
