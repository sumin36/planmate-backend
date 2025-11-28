package com.sumin.planmate.dto.dailytask;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DailyTaskRequestDto {

    @NotBlank
    private String title;
    private String description;

    @NotNull
    private LocalDate date;
}
