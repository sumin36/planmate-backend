package com.sumin.planmate.repository;

import com.sumin.planmate.entity.DailyTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyTaskRepository extends JpaRepository<DailyTask, Long> {
    List<DailyTask> findByUser_LoginIdAndDate(String loginId, LocalDate date);
}
