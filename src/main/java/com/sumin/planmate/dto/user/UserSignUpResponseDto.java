package com.sumin.planmate.dto.user;

import com.sumin.planmate.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignUpResponseDto {
    private String loginId;
    private String nickname;

    public static UserSignUpResponseDto toDto(User user) {
        return UserSignUpResponseDto.builder()
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .build();
    }
}
