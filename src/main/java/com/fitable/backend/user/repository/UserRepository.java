package com.fitable.backend.user.repository;

import com.fitable.backend.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.ageGroup = :ageGroup, u.gender = :gender, u.disabilityType = :disabilityType, u.disabilityLevel = :disabilityLevel WHERE u.loginId = :loginId")
    int updateUserInfoByLoginId(@Param("loginId") String loginId,
                                @Param("ageGroup") User.AgeGroup ageGroup,
                                @Param("gender") User.Gender gender,
                                @Param("disabilityType") User.DisabilityType disabilityType,
                                @Param("disabilityLevel") User.DisabilityLevel disabilityLevel);
}