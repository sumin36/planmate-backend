package com.sumin.planmate.dto.statistics;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import java.time.YearMonth;

@Getter
@SuperBuilder
public class MonthStatsDto extends BaseStatsDto {
    private YearMonth yearMonth;

    public static MonthStatsDto of(YearMonth yearMonth, long total, long completed, int rate) {
        return MonthStatsDto.builder()
                .yearMonth(yearMonth)
                .rate(rate)
                .totalCount(total)
                .completedCount(completed)
                .build();
    }
}
