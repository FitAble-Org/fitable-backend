package com.fitable.backend.user.dto;

import com.fitable.backend.user.entity.User;
import lombok.Data;

@Data
public class LoginRequest {
    private String loginId;
    private String password;
}
