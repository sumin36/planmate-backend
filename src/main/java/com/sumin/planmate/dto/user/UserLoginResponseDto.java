package com.sumin.planmate.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {
    private String username;
    private String accessToken;
    private String refreshToken;
    private String fullName;
}
