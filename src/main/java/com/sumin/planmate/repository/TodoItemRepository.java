package com.sumin.planmate.repository;

import com.sumin.planmate.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    void deleteByRoutineId(Long routineId);

    @Modifying
    @Query("delete from TodoItem t where t.routineId = :routineId and t.dailyTask.date >= :targetDate")
    void deleteFutureTodoItems(Long routineId, LocalDate targetDate);
}
