package com.sumin.planmate.controller;

import com.sumin.planmate.dto.dailytask.DailyTaskDto;
import com.sumin.planmate.dto.dailytask.DailyTaskRequestDto;
import com.sumin.planmate.dto.dailytask.DailyTaskUpdateDto;
import com.sumin.planmate.service.DailyTaskService;
import com.sumin.planmate.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/daily-task")
@RequiredArgsConstructor
public class DailyTaskController {

    private final DailyTaskService dailyTaskService;

    @PostMapping
    public ApiResponse<String> createDailyTask(@AuthenticationPrincipal String loginId,
                                               @Valid @RequestBody DailyTaskRequestDto dto) {
        dailyTaskService.addDailyTask(loginId, dto);
        return new ApiResponse<>(200, "Todo 추가 완료", null);
    }

    @GetMapping
    public ApiResponse<List<DailyTaskDto>> getDailyTasksByDate(@AuthenticationPrincipal String loginId,
                                                               @RequestParam(required = false) LocalDate date) {
        if(date == null) date = LocalDate.now();
        List<DailyTaskDto> list = dailyTaskService.getDailyTasksByDate(loginId, date);
        return new ApiResponse<>(200, "Todo 리스트 조회 완료", list);
    }

    @PutMapping("/{taskId}")
    public ApiResponse<DailyTaskDto> updateDailyTask(@PathVariable Long taskId,
                                               @Valid @RequestBody DailyTaskUpdateDto dto){
        DailyTaskDto updated = dailyTaskService.updateDailyTask(taskId, dto);
        return new ApiResponse<>(200, "Todo 업데이트 완료", updated);
    }

    @PatchMapping("/{taskId}/toggle")
    private ApiResponse<String> toggleDailyTask(@PathVariable Long taskId){
        dailyTaskService.toggleComplete(taskId);
        return new ApiResponse<>(200, "Todo 상태 업데이트 완료", null);
    }

    @DeleteMapping("/{taskId}")
    public ApiResponse<String> deleteDailyTask(@PathVariable Long taskId) {
        dailyTaskService.removeDailyTask(taskId);
        return new ApiResponse<>(200, "Todo 삭제 완료", null);
    }
}
