package com.sumin.planmate.dto.dailytask;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DailyTaskDto {
    private Long taskId;
    private String title;
    private String description;
    private LocalDate date;
    private boolean isCompleted;
}
