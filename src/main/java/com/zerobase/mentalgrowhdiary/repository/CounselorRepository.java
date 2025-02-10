package com.zerobase.mentalgrowhdiary.repository;

import com.zerobase.mentalgrowhdiary.domain.Counselor;
import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.repository.custom.CounselorRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long>, CounselorRepositoryCustom {

    Optional<Counselor> findByUser(User user);

    Optional<Counselor> findByUser_Username(String username);
}
