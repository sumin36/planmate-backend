package com.sumin.planmate.controller;

import com.sumin.planmate.dto.routine.RoutineDto;
import com.sumin.planmate.dto.routine.RoutineRequestDto;
import com.sumin.planmate.service.RoutineService;
import com.sumin.planmate.util.ApiResponse;
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
                              @RequestBody RoutineRequestDto dto) {
        routineService.addRoutine(loginId, dto);
        return new ApiResponse<>(200, "루틴 추가 성공", null);
    }

    @GetMapping
    public ApiResponse<List<RoutineDto>> getRoutines(@AuthenticationPrincipal String loginId) {
        List<RoutineDto> routines = routineService.getRoutines(loginId);
        return new ApiResponse<>(200, "루틴 리스트 조회 성공", routines);
    }
}
