package com.sumin.planmate.dto.statistics;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class BaseStatsDto {
    private int rate;
    private long totalCount;
    private long completedCount;
}