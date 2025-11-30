package com.sumin.planmate.controller;

import com.sumin.planmate.dto.routine.RoutineDto;
import com.sumin.planmate.dto.routine.RoutineRequestDto;
import com.sumin.planmate.dto.routine.RoutineUpdateDto;
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
    public ApiResponse<String> createRoutine(@AuthenticationPrincipal String loginId,
                                             @Valid @RequestBody RoutineRequestDto dto) {
        routineService.addRoutine(loginId, dto);
        return new ApiResponse<>(200, "루틴 추가 완료", null);
    }

    @GetMapping
    public ApiResponse<List<RoutineDto>> getRoutines(@AuthenticationPrincipal String loginId) {
        List<RoutineDto> routines = routineService.getRoutines(loginId);
        return new ApiResponse<>(200, "루틴 리스트 조회 완료", routines);
    }

    @PutMapping("/{routineId}")
    public ApiResponse<RoutineDto> updateDailyTask(@PathVariable Long routineId,
                                                     @Valid @RequestBody RoutineUpdateDto dto){
        RoutineDto updated = routineService.updateRoutine(routineId, dto);
        return new ApiResponse<>(200, "루틴 업데이트 완료", updated);
    }

    @PatchMapping("/{routineId}/toggle")
    private ApiResponse<String> toggleRoutine(@PathVariable Long routineId){
        routineService.toggleActive(routineId);
        return new ApiResponse<>(200, "루틴 활성 여부 업데이트 완료", null);
    }

    @DeleteMapping("/{routineId}")
    public ApiResponse<String> deleteDailyTask(@PathVariable Long routineId) {
        routineService.deleteRoutine(routineId);
        return new ApiResponse<>(200, "루틴 삭제 완료", null);
    }
}
