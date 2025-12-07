package com.sumin.planmate.dto.statistics;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.YearMonth;

@Getter
@SuperBuilder
public class MonthStatsDto extends BaseStatsDto {
    private YearMonth yearMonth;

    public static MonthStatsDto createZeroStats(YearMonth yearMonth) {
        return MonthStatsDto.builder()
                .yearMonth(yearMonth)
                .rate(0)
                .totalCount(0)
                .completedCount(0)
                .build();
    }
}
