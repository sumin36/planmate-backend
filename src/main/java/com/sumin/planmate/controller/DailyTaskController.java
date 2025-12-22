package com.sumin.planmate.controller;

import com.sumin.planmate.dto.dailytask.*;
import com.sumin.planmate.dto.user.CustomUserDetails;
import com.sumin.planmate.service.DailyTaskService;
import com.sumin.planmate.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "할 일 생성", description = "새로운 할 일을 추가합니다.")
    @PostMapping
    public ApiResponse<TodoItemDto> createTodoItem(@Valid @RequestBody TodoItemRequestDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        TodoItemDto task = dailyTaskService.addSingleTodoItem(dto, userDetails.getUserId());
        return new ApiResponse<>(200, "todoItem 추가 완료", task);
    }

    @Operation(summary = "날짜별 할 일 목록 조회", description = "특정 날짜의 할 일 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<DailyTaskDto> getDailyTasksByDate(@RequestParam(required = false) LocalDate date,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(date == null) date = LocalDate.now();
        DailyTaskDto tasks = dailyTaskService.getDailyTaskByDate(date, userDetails.getUserId());
        return new ApiResponse<>(200, "daily task 조회 완료", tasks);
    }

    @Operation(summary = "할 일 내용 수정", description = "할 일의 제목이나 상세 내용을 수정합니다.")
    @PutMapping("/{itemId}")
    public ApiResponse<TodoItemDto> updateTodoItem(@PathVariable Long itemId,
                                                   @RequestBody TodoItemUpdateDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails){
        TodoItemDto updated = dailyTaskService.updateTodoItem(itemId, dto, userDetails.getUserId());
        return new ApiResponse<>(200, "todoItem 업데이트 완료", updated);
    }

    @Operation(summary = "알람 시간 추가", description = "특정 할 일의 알람 시간을 추가하거나 변경합니다.")
    @PatchMapping("/{itemId}/alarm")
    public ApiResponse<TodoItemDto> updateTodoItemAlarmTime(@PathVariable Long itemId,
                                                            @Valid @RequestBody TodoAlarmUpdateDto dto,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails){
        TodoItemDto updated = dailyTaskService.updateAlarmTime(itemId, dto, userDetails.getUserId());
        return new ApiResponse<>(200, "todoItem 알람 업데이트 완료", updated);
    }

    @Operation(summary = "할 일 완료 상태 토글", description = "할 일의 완료 여부를 반전시킵니다.")
    @PatchMapping("/{itemId}/toggle")
    private ApiResponse<TodoItemDto> toggleTodoItem(@PathVariable Long itemId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        TodoItemDto task = dailyTaskService.toggleComplete(itemId, userDetails.getUserId());
        return new ApiResponse<>(200, "todoItem 상태 업데이트 완료", task);
    }

    @Operation(summary = "할 일 삭제", description = "할 일을 삭제합니다.")
    @DeleteMapping("/{itemId}")
    public ApiResponse<String> deleteTodoItem(@PathVariable Long itemId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        dailyTaskService.removeTodoItem(itemId, userDetails.getUserId());
        return new ApiResponse<>(200, "todoItem 삭제 완료", null);
    }
}
