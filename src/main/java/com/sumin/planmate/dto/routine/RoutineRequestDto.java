package com.sumin.planmate.dto.routine;

import com.sumin.planmate.entity.RepeatType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RoutineRequestDto {

    @NotBlank
    private String title;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private RepeatType repeatType;
    private String repeatDescription;

    @Min(0) @Max(23)
    private Integer hour;

    @Min(0) @Max(59)
    private Integer minute;
}

