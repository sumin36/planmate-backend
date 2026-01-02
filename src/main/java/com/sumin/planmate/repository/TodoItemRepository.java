package com.sumin.planmate.repository;

import com.sumin.planmate.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    @Query("select t from TodoItem t join fetch t.dailyTask d join fetch d.user u where t.id = :todoItemId")
    Optional<TodoItem> findByIdWithDailyTaskAndUser(Long todoItemId);

    @Modifying
    @Query("delete from TodoItem t where t.routineId =:routineId")
    void deleteByRoutineId(Long routineId);

    @Modifying
    @Query("delete from TodoItem t where t.routineId = :routineId and t.dailyTask.date >= :targetDate")
    void deleteFutureTodoItems(Long routineId, LocalDate targetDate);

    List<TodoItem> findByRoutineIdIn(List<Long> routineIds);

    @Query("select t from TodoItem t " +
            "join fetch t.dailyTask d " +
            "join fetch d.user u " +
            "where d.date = :date and t.alarmTime = :time")
    List<TodoItem> findByDateAndAlarmTimeWithUser(LocalDate date, LocalTime time);
}
