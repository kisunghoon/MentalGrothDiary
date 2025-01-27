package com.zerobase.mentalgrowhdiary.repository;

import com.zerobase.mentalgrowhdiary.domain.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {

    Optional<Counselor> findByUser_UserId(Long userId);
}
