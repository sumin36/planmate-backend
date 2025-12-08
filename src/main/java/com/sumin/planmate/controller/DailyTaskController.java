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
        TodoItemDto task = dailyTaskService.addSingleTodoItem(dto, userDetails.getUserId());
        return new ApiResponse<>(200, "todoItem 추가 완료", task);
    }

    @GetMapping
    public ApiResponse<DailyTaskDto> getDailyTasksByDate(@RequestParam(required = false) LocalDate date,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(date == null) date = LocalDate.now();
        DailyTaskDto tasks = dailyTaskService.getDailyTasksByDate(date, userDetails.getUserId());
        return new ApiResponse<>(200, "daily task 조회 완료", tasks);
    }

    @PutMapping("/{itemId}")
    public ApiResponse<TodoItemDto> updateTodoItem(@PathVariable Long itemId,
                                                   @Valid @RequestBody TodoItemUpdateDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails){
        TodoItemDto updated = dailyTaskService.updateDailyTask(itemId, dto, userDetails.getUserId());
        return new ApiResponse<>(200, "todoItem 업데이트 완료", updated);
    }

    @PatchMapping("/{itemId}/toggle")
    private ApiResponse<TodoItemDto> toggleTodoItem(@PathVariable Long itemId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        TodoItemDto task = dailyTaskService.toggleComplete(itemId, userDetails.getUserId());
        return new ApiResponse<>(200, "todoItem 상태 업데이트 완료", task);
    }

    @DeleteMapping("/{itemId}")
    public ApiResponse<String> deleteTodoItem(@PathVariable Long itemId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        dailyTaskService.removeTodoItem(itemId, userDetails.getUserId());
        return new ApiResponse<>(200, "todoItem 삭제 완료", null);
    }
}
