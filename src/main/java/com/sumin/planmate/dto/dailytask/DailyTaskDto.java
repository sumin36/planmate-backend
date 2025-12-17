package com.sumin.planmate.dto.dailytask;

import com.sumin.planmate.entity.DailyTask;
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

    public static DailyTaskDto of(LocalDate date, List<TodoItemDto> items){
        return new DailyTaskDto(date, items);
    }

    public static DailyTaskDto from(DailyTask dailyTask) {
        return new DailyTaskDto(
                dailyTask.getDate(),
                dailyTask.getTodoItems().stream().map(TodoItemDto::from).toList()
        );
    }
}