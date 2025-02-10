package com.zerobase.mentalgrowhdiary.service;

import com.zerobase.mentalgrowhdiary.domain.Counselor;
import com.zerobase.mentalgrowhdiary.domain.Diary;
import com.zerobase.mentalgrowhdiary.domain.Evaluation;
import com.zerobase.mentalgrowhdiary.dto.EvaluationRequest;
import com.zerobase.mentalgrowhdiary.dto.EvaluationResponse;
import com.zerobase.mentalgrowhdiary.exception.MentalGrowthException;
import com.zerobase.mentalgrowhdiary.repository.CounselorRepository;
import com.zerobase.mentalgrowhdiary.repository.DiaryRepository;
import com.zerobase.mentalgrowhdiary.repository.EvaluationRepository;
import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final DiaryRepository diaryRepository;
    private final CounselorRepository counselorRepository;

    @Transactional
    public void registerEvaluation(EvaluationRequest request, String counselorName) {


        Counselor counselor = counselorRepository.findByUser_Username(counselorName)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.COUNSELOR_NOT_FOUND));

        if(!counselor.getCounselorId().equals(request.getCounselorId())) {
            throw new MentalGrowthException(ErrorCode.COUNSELOR_NOT_FOUND," 인증된 상담사와 요청된 상담사의 아이디가 불일치 합니다.");
        }

        Diary diary = diaryRepository.findById(request.getDiaryId())
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.DIARY_NOT_FOUND));

        Evaluation evaluation = Evaluation.builder()
                                            .diary(diary)
                                            .counselor(counselor)
                                            .assessment(request.getAssessment())
                                            .build();

        evaluationRepository.save(evaluation);
    }

    @Transactional
    public void updateEvaluation(Long evaluationId,EvaluationRequest request,String counselorName) {

        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.EVALUATION_NOT_FOUND_ID));

        if(!evaluation.getCounselor().getUser().getUsername().equals(counselorName)){
            throw new MentalGrowthException(ErrorCode.FORBIDDEN);
        }

        evaluation.setAssessment(request.getAssessment());
        evaluation.setUpdatedDate(LocalDateTime.now());


        evaluationRepository.save(evaluation);
    }

    @Transactional
    public void deleteEvaluation(Long evaluationId, String counselorName) {

        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.EVALUATION_NOT_FOUND_ID));

        if(!evaluation.getCounselor().getUser().getUsername().equals(counselorName)){
            throw new MentalGrowthException(ErrorCode.FORBIDDEN);
        }

        evaluationRepository.delete(evaluation);
    }

    @Transactional(readOnly = true)
    public Page<EvaluationResponse> getEvaluations(String counselorName, Pageable pageable) {

        Counselor counselor = counselorRepository.findByUser_Username(counselorName)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.COUNSELOR_NOT_FOUND));

        return evaluationRepository.findByCounselor(counselor,pageable)
                .map(evaluation -> new EvaluationResponse(
                        evaluation.getEvaluationId(),
                        evaluation.getDiary().getDiaryContent(),
                        evaluation.getCounselor().getCounselorId(),
                        evaluation.getAssessment(),
                        evaluation.getCreatedDate(),
                        evaluation.getUpdatedDate()
                ));
    }
}
