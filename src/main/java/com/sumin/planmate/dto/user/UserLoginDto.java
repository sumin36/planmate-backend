package com.sumin.planmate.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginDto {

    @NotBlank
    public String username;

    @NotBlank
    public String password;
}
