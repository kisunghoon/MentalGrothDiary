package com.zerobase.mentalgrowhdiary.repository;

import com.zerobase.mentalgrowhdiary.domain.Counselor;
import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.domain.UserCounselor;
import com.zerobase.mentalgrowhdiary.repository.custom.UserCounselorRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserCounselorRepository extends JpaRepository<UserCounselor, Long> , UserCounselorRepositoryCustom {

    boolean existsByUserAndCounselor(User user, Counselor counselor);
    boolean existsByUserAndActive(User user,Boolean active);

    Optional<UserCounselor> findByUserAndActive(User user, Boolean active);

}
