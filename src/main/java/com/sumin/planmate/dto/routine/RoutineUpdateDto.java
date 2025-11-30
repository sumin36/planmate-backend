package com.sumin.planmate.dto.routine;

import com.sumin.planmate.entity.RepeatType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RoutineUpdateDto {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private RepeatType repeatType;
    private String repeatDescription;

    @Min(0) @Max(23)
    private Integer hour;

    @Min(0) @Max(59)
    private Integer minute;
}
