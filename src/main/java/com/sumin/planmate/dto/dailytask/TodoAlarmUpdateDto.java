package com.sumin.planmate.dto.dailytask;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class TodoAlarmUpdateDto {

    @Min(0) @Max(23)
    private Integer hour;

    @Min(0) @Max(59)
    private Integer minute;
}
