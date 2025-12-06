package com.sumin.planmate.repository;

import com.sumin.planmate.entity.DailyTask;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyTaskRepository extends JpaRepository<DailyTask, Long> {
    @EntityGraph(attributePaths = {"todoItems"})
    Optional<DailyTask> findByUserIdAndDate(Long userId, LocalDate date);

    List<DailyTask> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
