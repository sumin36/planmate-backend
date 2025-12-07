package com.sumin.planmate.dto.statistics;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class RoutineStatsDto extends BaseStatsDto{
    private Long routineId;
    private String title;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public static RoutineStatsDto createZeroStats(Long routineId, String title, LocalDate startDate, LocalDate endDate) {
        return RoutineStatsDto.builder()
                .routineId(routineId)
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .rate(0)
                .totalCount(0)
                .completedCount(0)
                .build();
    }
}
