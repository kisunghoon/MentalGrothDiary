package com.zerobase.mentalgrowhdiary.repository;

import com.zerobase.mentalgrowhdiary.domain.Counselor;
import com.zerobase.mentalgrowhdiary.domain.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    Page<Evaluation> findByCounselor(Counselor c, Pageable pageable);
}
