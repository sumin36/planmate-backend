package com.sumin.planmate.dto.user;

import com.sumin.planmate.entity.Gender;
import com.sumin.planmate.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserInfoDto {
    private String username;
    private String fullName;
    private String email;
    private Gender gender;
    private LocalDate birthDate;

    public static UserInfoDto from(User user) {
        return UserInfoDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .build();
    }
}