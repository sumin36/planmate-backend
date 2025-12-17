package com.sumin.planmate.dto.statistics;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import java.time.Year;

@Getter
@SuperBuilder
public class YearStatsDto extends BaseStatsDto {
    private Year year;

    public static YearStatsDto of(Year year, long total, long completed, int rate) {
        return YearStatsDto.builder()
                .year(year)
                .rate(rate)
                .totalCount(total)
                .completedCount(completed)
                .build();
    }
}
