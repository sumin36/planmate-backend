package com.sumin.planmate.dto.statistics;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Year;

@Getter
@SuperBuilder
public class YearStatsDto extends BaseStatsDto {
    private Year year;

    public static YearStatsDto createZeroStats(Year year) {
        return YearStatsDto.builder()
                .year(year)
                .rate(0)
                .totalCount(0)
                .completedCount(0)
                .build();
    }
}
