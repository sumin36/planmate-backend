package com.sumin.planmate.dto.user;

import com.sumin.planmate.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserSignupDto {

    @NotBlank
    @Size(min = 3, max = 20)
    private String loginId;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    @Email
    private String email;

    private Gender gender;
    private LocalDate birthDate;
}
