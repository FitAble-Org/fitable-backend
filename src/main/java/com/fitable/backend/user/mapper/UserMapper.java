package com.fitable.backend.user.mapper;

import com.fitable.backend.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByLoginId(@Param("loginId") String loginId);

    boolean existsByLoginId(@Param("loginId") String loginId);

    void registerUser(User user);

    int updateUserInfoByLoginId(@Param("loginId") String loginId,
                                @Param("ageGroup") User.AgeGroup ageGroup,
                                @Param("gender") User.Gender gender,
                                @Param("disabilityType") User.DisabilityType disabilityType,
                                @Param("disabilityLevel") User.DisabilityLevel disabilityLevel);
}
