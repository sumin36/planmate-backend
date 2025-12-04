package com.sumin.planmate.controller;

import com.sumin.planmate.dto.routine.RoutineDto;
import com.sumin.planmate.dto.routine.RoutineRequestDto;
import com.sumin.planmate.dto.routine.RoutineUpdateDto;
import com.sumin.planmate.dto.user.CustomUserDetails;
import com.sumin.planmate.service.RoutineService;
import com.sumin.planmate.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routine")
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping
    public ApiResponse<String> createRoutine(@Valid @RequestBody RoutineRequestDto dto,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        routineService.addRoutine(dto, userDetails.getUserId());
        return new ApiResponse<>(200, "루틴 추가 완료", null);
    }

    @GetMapping
    public ApiResponse<List<RoutineDto>> getRoutines(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<RoutineDto> routines = routineService.getRoutines(userDetails.getUserId());
        return new ApiResponse<>(200, "루틴 리스트 조회 완료", routines);
    }

    @PutMapping("/{routineId}")
    public ApiResponse<RoutineDto> updateDailyTask(@PathVariable Long routineId,
                                                   @Valid @RequestBody RoutineUpdateDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails){
        RoutineDto updated = routineService.updateRoutine(routineId, dto, userDetails.getUserId());
        return new ApiResponse<>(200, "루틴 업데이트 완료", updated);
    }

    @DeleteMapping("/{routineId}")
    public ApiResponse<String> deleteDailyTask(@PathVariable Long routineId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        routineService.deleteRoutine(routineId, userDetails.getUserId());
        return new ApiResponse<>(200, "루틴 삭제 완료", null);
    }
}
