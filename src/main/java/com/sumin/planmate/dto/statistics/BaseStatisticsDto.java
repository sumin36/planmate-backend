package com.sumin.planmate.dto.statistics;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class BaseStatisticsDto {
    private int rate;
    private long totalCount;
    private long completedCount;
}