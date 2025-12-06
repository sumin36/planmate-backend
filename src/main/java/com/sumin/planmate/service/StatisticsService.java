package com.sumin.planmate.service;

import com.sumin.planmate.dto.statistics.DailyStatsDto;
import com.sumin.planmate.dto.statistics.MonthStatsDto;
import com.sumin.planmate.dto.statistics.YearStatsDto;
import com.sumin.planmate.entity.DailyTask;
import com.sumin.planmate.entity.TodoItem;
import com.sumin.planmate.repository.DailyTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final DailyTaskRepository dailyTaskRepository;

    public DailyStatsDto getDailyRate(LocalDate date, Long userId) {
        DailyTask task = dailyTaskRepository.findByUserIdAndDate(userId, date).orElse(null);

        if(task == null) {
            return DailyStatsDto.createZeroStats(date);
        }

        long total = task.getTodoItems().size();
        long completed = task.getTodoItems().stream().filter(TodoItem::getIsCompleted).count();
        int rate = total == 0 ? 0 : (int) (completed * 100 / total);

        return DailyStatsDto.builder()
                .date(date)
                .rate(rate)
                .totalCount(total)
                .completedCount(completed)
                .build();
    }

    public MonthStatsDto getMonthlyRate(YearMonth yearMonth, Long userId) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<DailyTask> tasks = dailyTaskRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        
        if(tasks.isEmpty()) {
           return MonthStatsDto.createZeroStats(yearMonth);
        }

        StatisticsData data = calculateStatistics(tasks);

        return MonthStatsDto.builder()
                .yearMonth(yearMonth)
                .rate(data.rate())
                .totalCount(data.total())
                .completedCount(data.completed())
                .build();
    }

    public YearStatsDto getYearlyRate(Year year, Long userId) {
        LocalDate startDate = year.atDay(1);
        LocalDate endDate = year.atMonth(java.time.Month.DECEMBER).atEndOfMonth();

        List<DailyTask> tasks = dailyTaskRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        if(tasks.isEmpty()) {
            return YearStatsDto.createZeroStats(year);
        }

        StatisticsData data = calculateStatistics(tasks);

        return YearStatsDto.builder()
                .year(year)
                .rate(data.rate())
                .totalCount(data.total())
                .completedCount(data.completed())
                .build();
    }

    private StatisticsData calculateStatistics(List<DailyTask> tasks) {
        long total = tasks.stream()
                .mapToLong(task -> task.getTodoItems().size())
                .sum();
        long completed = tasks.stream()
                .mapToLong(task -> task.getTodoItems().stream().filter(TodoItem::getIsCompleted).count())
                .sum();

        int rate = total == 0 ? 0 : (int) (completed * 100 / total);
        return new StatisticsData(total, completed, rate);
    }

    private record StatisticsData(long total, long completed, int rate) {}
}
