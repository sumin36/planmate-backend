package com.sumin.planmate.dto.dailytask;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class TodoItemDto {
    private Long id;
    private String title;
    private String memo;
    private boolean isCompleted;
    private LocalTime alarmTime;
    private Long routineId;
}