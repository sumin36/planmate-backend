package com.sumin.planmate.dto.statistics;

import com.sumin.planmate.entity.DailyTask;
import com.sumin.planmate.entity.TodoItem;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class DailyStatsDto extends BaseStatsDto {
    private LocalDate date;

    public static DailyStatsDto from(DailyTask dailyTask) {
        long total = dailyTask.getTodoItems().size();
        long completed = dailyTask.getTodoItems().stream().filter(TodoItem::getIsCompleted).count();
        int rate = total == 0 ? 0 : (int) (completed * 100 / total);

        return DailyStatsDto.builder()
                .date(dailyTask.getDate())
                .rate(rate)
                .totalCount(total)
                .completedCount(completed)
                .build();
    }
}
