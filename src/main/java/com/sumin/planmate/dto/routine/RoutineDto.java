package com.sumin.planmate.dto.routine;

import com.sumin.planmate.entity.RepeatType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class RoutineDto {
    private Long routineId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private RepeatType repeatType;
    private String repeatDescription;
    private LocalTime alarmTime;
}
