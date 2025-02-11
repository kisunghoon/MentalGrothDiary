package com.zerobase.mentalgrowhdiary.repository;

import com.zerobase.mentalgrowhdiary.domain.FeedbackRequestHistorty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRequestHistortyRepository extends JpaRepository<FeedbackRequestHistorty, Long> {
}
