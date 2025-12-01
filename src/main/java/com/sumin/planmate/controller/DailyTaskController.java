package com.sumin.planmate.controller;

import com.sumin.planmate.dto.dailytask.DailyTaskDto;
import com.sumin.planmate.dto.dailytask.TodoItemRequestDto;
import com.sumin.planmate.dto.dailytask.TodoItemUpdateDto;
import com.sumin.planmate.dto.dailytask.TodoItemDto;
import com.sumin.planmate.service.DailyTaskService;
import com.sumin.planmate.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/daily-task")
@RequiredArgsConstructor
public class DailyTaskController {

    private final DailyTaskService dailyTaskService;

    @PostMapping
    public ApiResponse<TodoItemDto> createTodoItem(@AuthenticationPrincipal String loginId,
                                                   @Valid @RequestBody TodoItemRequestDto dto) {
        TodoItemDto task = dailyTaskService.addTodoItem(loginId, dto);
        return new ApiResponse<>(200, "task 추가 완료", task);
    }

    @GetMapping
    public ApiResponse<DailyTaskDto> getDailyTasksByDate(@AuthenticationPrincipal String loginId,
                                                               @RequestParam(required = false) LocalDate date) {
        if(date == null) date = LocalDate.now();
        DailyTaskDto tasks = dailyTaskService.getDailyTasksByDate(loginId, date);
        return new ApiResponse<>(200, "daily task 조회 완료", tasks);
    }

    @PutMapping("/{taskId}")
    public ApiResponse<TodoItemDto> updateTodoItem(@PathVariable Long taskId,
                                                   @Valid @RequestBody TodoItemUpdateDto dto){
        TodoItemDto updated = dailyTaskService.updateDailyTask(taskId, dto);
        return new ApiResponse<>(200, "task 업데이트 완료", updated);
    }

    @PatchMapping("/{taskId}/toggle")
    private ApiResponse<TodoItemDto> toggleTodoItem(@PathVariable Long taskId){
        TodoItemDto task = dailyTaskService.toggleComplete(taskId);
        return new ApiResponse<>(200, "task 상태 업데이트 완료", task);
    }

    @DeleteMapping("/{taskId}")
    public ApiResponse<String> deleteTodoItem(@PathVariable Long taskId) {
        dailyTaskService.removeTodoItem(taskId);
        return new ApiResponse<>(200, "task 삭제 완료", null);
    }
}
