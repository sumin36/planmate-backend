package com.sumin.planmate.service;

import com.sumin.planmate.dto.statistics.DailyStatsDto;
import com.sumin.planmate.dto.statistics.MonthStatsDto;
import com.sumin.planmate.dto.statistics.RoutineStatsDto;
import com.sumin.planmate.dto.statistics.YearStatsDto;
import com.sumin.planmate.entity.DailyTask;
import com.sumin.planmate.entity.Routine;
import com.sumin.planmate.entity.TodoItem;
import com.sumin.planmate.repository.DailyTaskRepository;
import com.sumin.planmate.repository.RoutineRepository;
import com.sumin.planmate.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final DailyTaskRepository dailyTaskRepository;
    private final RoutineRepository routineRepository;
    private final TodoItemRepository todoItemRepository;

    public DailyStatsDto getDailyRate(LocalDate date, Long userId) {
        return dailyTaskRepository.findByUserIdAndDate(userId, date)
                .map(DailyStatsDto::from)
                .orElse(DailyStatsDto.builder().date(date).build());
    }

    public MonthStatsDto getMonthlyRate(YearMonth yearMonth, Long userId) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<DailyTask> tasks = dailyTaskRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        
        if(tasks.isEmpty()) {
           return MonthStatsDto.of(yearMonth, 0, 0, 0);
        }

        StatisticsData data = StatisticsData.of(tasks);
        return MonthStatsDto.of(yearMonth, data.total, data.completed, data.rate);
    }

    public YearStatsDto getYearlyRate(Year year, Long userId) {
        LocalDate startDate = year.atDay(1);
        LocalDate endDate = year.atMonth(java.time.Month.DECEMBER).atEndOfMonth();

        List<DailyTask> tasks = dailyTaskRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        if(tasks.isEmpty()) {
            return YearStatsDto.of(year, 0, 0, 0);
        }

        StatisticsData data = StatisticsData.of(tasks);
        return YearStatsDto.of(year, data.total, data.completed, data.rate);
    }

    // 루틴별 달성률 리스트 조회
    public List<RoutineStatsDto> getRoutineRates(Long userId) {
        List<Routine> routines = routineRepository.findByUserId(userId);

        if(routines.isEmpty()) {
            return Collections.emptyList();
        }

        // 모든 루틴별 TodoItem 리스트 가져와서 저장(Map)
        List<Long> routineIds = routines.stream().map(Routine::getId).toList();
        List<TodoItem> allRoutineItems = todoItemRepository.findByRoutineIdIn(routineIds);

        Map<Long, List<TodoItem>> todoItemsByRoutineId = allRoutineItems.stream()
                .collect(Collectors.groupingBy(TodoItem::getRoutineId));

        return routines.stream()
                .map(routine -> RoutineStatsDto.from(
                        routine,
                        todoItemsByRoutineId.getOrDefault(routine.getId(), Collections.emptyList())))
                .toList();
    }

    // 통계 데이터(전체 개수, 완료 개수, 달성률)
    private record StatisticsData(long total, long completed, int rate) {
        public static StatisticsData of(List<DailyTask> tasks) {
            long total = tasks.stream()
                    .mapToLong(task -> task.getTodoItems().size())
                    .sum();
            long completed = tasks.stream()
                    .mapToLong(task -> task.getTodoItems().stream()
                            .filter(TodoItem::getIsCompleted).count())
                    .sum();
            int rate = total == 0 ? 0 : (int) (completed * 100 / total);

            return new StatisticsData(total, completed, rate);
        }
    }
}
