package com.sumin.planmate.controller;

import com.sumin.planmate.dto.dailytask.DailyTaskDto;
import com.sumin.planmate.dto.dailytask.TodoItemRequestDto;
import com.sumin.planmate.dto.dailytask.TodoItemUpdateDto;
import com.sumin.planmate.dto.dailytask.TodoItemDto;
import com.sumin.planmate.dto.user.CustomUserDetails;
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
    public ApiResponse<TodoItemDto> createTodoItem(@Valid @RequestBody TodoItemRequestDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        TodoItemDto task = dailyTaskService.addTodoItem(dto, null, userDetails.getUserId());
        return new ApiResponse<>(200, "task 추가 완료", task);
    }

    @GetMapping
    public ApiResponse<DailyTaskDto> getDailyTasksByDate(@RequestParam(required = false) LocalDate date,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(date == null) date = LocalDate.now();
        DailyTaskDto tasks = dailyTaskService.getDailyTasksByDate(date, userDetails.getUserId());
        return new ApiResponse<>(200, "daily task 조회 완료", tasks);
    }

    @PutMapping("/{taskId}")
    public ApiResponse<TodoItemDto> updateTodoItem(@PathVariable Long taskId,
                                                   @Valid @RequestBody TodoItemUpdateDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails){
        TodoItemDto updated = dailyTaskService.updateDailyTask(taskId, dto, userDetails.getUserId());
        return new ApiResponse<>(200, "task 업데이트 완료", updated);
    }

    @PatchMapping("/{taskId}/toggle")
    private ApiResponse<TodoItemDto> toggleTodoItem(@PathVariable Long taskId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        TodoItemDto task = dailyTaskService.toggleComplete(taskId, userDetails.getUserId());
        return new ApiResponse<>(200, "task 상태 업데이트 완료", task);
    }

    @DeleteMapping("/{taskId}")
    public ApiResponse<String> deleteTodoItem(@PathVariable Long taskId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        dailyTaskService.removeTodoItem(taskId, userDetails.getUserId());
        return new ApiResponse<>(200, "task 삭제 완료", null);
    }
}
