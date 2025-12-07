package com.sumin.planmate.controller;

import com.sumin.planmate.dto.statistics.DailyStatsDto;
import com.sumin.planmate.dto.statistics.MonthStatsDto;
import com.sumin.planmate.dto.statistics.RoutineStatsDto;
import com.sumin.planmate.dto.statistics.YearStatsDto;
import com.sumin.planmate.dto.user.CustomUserDetails;
import com.sumin.planmate.service.StatisticsService;
import com.sumin.planmate.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/daily")
    public ApiResponse<DailyStatsDto> getDailyStats(@RequestParam LocalDate date,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        DailyStatsDto dto = statisticsService.getDailyRate(date, userDetails.getUserId());
        return new ApiResponse<>(200, "일별 통계 조회 완료", dto);
    }

    @GetMapping("/month")
    public ApiResponse<MonthStatsDto> getMonthlyStats(@RequestParam YearMonth yearMonth,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        MonthStatsDto dto = statisticsService.getMonthlyRate(yearMonth, userDetails.getUserId());
        return new ApiResponse<>(200, "월별 통계 조회 완료", dto);
    }

    @GetMapping("/year")
    public ApiResponse<YearStatsDto> getYearlyStats(@RequestParam Year year,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        YearStatsDto dto = statisticsService.getYearlyRate(year, userDetails.getUserId());
        return new ApiResponse<>(200, "연도별 통계 조회 완료", dto);
    }

    @GetMapping("/routine")
    public ApiResponse<List<RoutineStatsDto>> getRoutineStats(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<RoutineStatsDto> dtos = statisticsService.getRoutineRates(userDetails.getUserId());
        return new ApiResponse<>(200, "루틴 통계 리스트 조회 완료", dtos);
    }
}
