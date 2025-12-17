package com.sumin.planmate.dto.dailytask;

import com.sumin.planmate.entity.TodoItem;
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

    public static TodoItemDto from(TodoItem todoItem){
        return TodoItemDto.builder()
                .id(todoItem.getId())
                .title(todoItem.getTitle())
                .memo(todoItem.getMemo())
                .isCompleted(todoItem.getIsCompleted())
                .alarmTime(todoItem.getAlarmTime())
                .routineId(todoItem.getRoutineId())
                .build();
    }
}