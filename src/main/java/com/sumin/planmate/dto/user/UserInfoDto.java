package com.sumin.planmate.dto.user;

import com.sumin.planmate.entity.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserInfoDto {
    private String loginId;
    private String nickname;
    private String email;
    private Gender gender;
    private LocalDate birthDate;
}