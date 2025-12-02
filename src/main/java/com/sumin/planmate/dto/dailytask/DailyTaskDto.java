package com.sumin.planmate.dto.dailytask;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyTaskDto {
    private LocalDate date;
    private List<TodoItemDto> todoItems;

    public static DailyTaskDto create(LocalDate date, List<TodoItemDto> items){
        return new DailyTaskDto(date, items);
    }
}