package com.sumin.planmate.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginDto {

    @NotBlank
    public String loginId;

    @NotBlank
    public String password;
}
