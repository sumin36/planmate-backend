package com.sumin.planmate.dto.statistics;

import com.sumin.planmate.entity.Routine;
import com.sumin.planmate.entity.TodoItem;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@SuperBuilder
public class RoutineStatsDto extends BaseStatsDto{
    private Long routineId;
    private String title;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public static RoutineStatsDto from(Routine routine, List<TodoItem> items) {
        long total = items.size();
        long completed = items.stream().filter(TodoItem::getIsCompleted).count();
        int rate = total == 0 ? 0 : (int) (completed * 100 / total);

        return RoutineStatsDto.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .startDate(routine.getStartDate())
                .endDate(routine.getEndDate())
                .rate(rate)
                .totalCount(total)
                .completedCount(completed)
                .build();
    }
}
