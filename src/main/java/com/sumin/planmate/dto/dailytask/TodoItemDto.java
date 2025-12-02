package com.sumin.planmate.dto.dailytask;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoItemDto {
    private Long id;
    private String title;
    private String description;
    private boolean isCompleted;
    private Long routineId;
}