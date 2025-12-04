package com.sumin.planmate.dto.user;

import com.sumin.planmate.entity.Gender;
import jakarta.validation.constraints.Email;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserUpdateDto {
    private String fullName;

    @Email
    private String email;
    private Gender gender;
    private LocalDate birthDate;
}
