package com.sumin.planmate.dto.statistics;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class DailyStatsDto extends BaseStatsDto {
    private LocalDate date;

    public static DailyStatsDto createZeroStats(LocalDate date) {
        return DailyStatsDto.builder()
                .date(date)
                .rate(0)
                .totalCount(0)
                .completedCount(0)
                .build();
    }
}
