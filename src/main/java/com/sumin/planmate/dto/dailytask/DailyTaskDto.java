package com.sumin.planmate.dto.dailytask;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class DailyTaskDto {
    private LocalDate date;
    private List<TodoItemDto> todoItems;
}