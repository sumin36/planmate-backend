package com.sumin.planmate.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordChangeDto {
    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}
