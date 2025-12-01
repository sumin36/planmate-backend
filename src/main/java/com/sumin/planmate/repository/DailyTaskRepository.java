package com.sumin.planmate.repository;

import com.sumin.planmate.entity.DailyTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyTaskRepository extends JpaRepository<DailyTask, Long> {
    Optional<DailyTask> findByUserLoginIdAndDate(String loginId, LocalDate date);
}
