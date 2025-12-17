package com.sumin.planmate.dto.routine;

import com.sumin.planmate.entity.RepeatType;
import com.sumin.planmate.entity.Routine;
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

    public static RoutineDto from(Routine routine) {
        return RoutineDto.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .startDate(routine.getStartDate())
                .endDate(routine.getEndDate())
                .repeatType(routine.getRepeatType())
                .repeatDescription(routine.getRepeatDescription())
                .alarmTime(routine.getAlarmTime())
                .build();
    }
}
