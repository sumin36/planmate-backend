package com.sumin.planmate.dto.dailytask;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DailyTaskUpdateDto {
    private String title;
    private String description;
    private LocalDate date;
}
