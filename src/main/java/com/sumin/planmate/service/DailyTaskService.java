package com.sumin.planmate.service;

import com.sumin.planmate.dto.dailytask.*;
import com.sumin.planmate.entity.DailyTask;
import com.sumin.planmate.entity.TodoItem;
import com.sumin.planmate.entity.User;
import com.sumin.planmate.exception.NotFoundException;
import com.sumin.planmate.repository.DailyTaskRepository;
import com.sumin.planmate.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyTaskService {

    private final UserService userService;
    private final DailyTaskRepository dailyTaskRepository;
    private final TodoItemRepository todoItemRepository;

    // 단일 TodoItem 추가
    public TodoItemDto addSingleTodoItem(TodoItemRequestDto dto, Long userId){
        User user = userService.getUser(userId);
        DailyTask dailyTask = dailyTaskRepository.findByUserIdAndDate(userId, dto.getDate())
                .orElseGet(() -> {
                    DailyTask newTask = DailyTask.builder().date(dto.getDate()).build();
                    user.addDailyTask(newTask);
                    return newTask;
                });

        TodoItem todoItem = createTodo(dto);
        dailyTask.addTodoItem(todoItem);
        todoItemRepository.save(todoItem);
        return TodoItemDto.from(todoItem);
    }

    // 루틴에 맞게 TodoItem 추가
    public void addTodoItemForRoutine(TodoItemRequestDto dto, LocalTime alarmTime, Long routineId,
                                      User user, Map<LocalDate, DailyTask> taskMap){

        DailyTask dailyTask = taskMap.get(dto.getDate());
        if(dailyTask == null){
            dailyTask = DailyTask.builder().date(dto.getDate()).build();

            user.addDailyTask(dailyTask);
            taskMap.put(dto.getDate(), dailyTask);
        }
        TodoItem todoItem = createRoutineTodo(dto, alarmTime, routineId);
        dailyTask.addTodoItem(todoItem);
    }

    // 날짜별 TodoItem 리스트 조회
    @Transactional(readOnly = true)
    public DailyTaskDto getDailyTaskByDate(LocalDate date, Long userId){
        DailyTask task = dailyTaskRepository.findByUserIdAndDate(userId, date)
                .orElse(null);

        if(task == null){
            return DailyTaskDto.of(date, List.of());
        }
        return DailyTaskDto.from(task);
    }

    // TodoItem 수정
    public TodoItemDto updateTodoItem(Long todoItemId, TodoItemUpdateDto dto, Long userId){
        TodoItem todoItem = getTodoItem(todoItemId);
        validateOwnership(userId, todoItem);

        todoItem.updateContent(dto.getTitle(), dto.getMemo());

        return TodoItemDto.from(todoItem);
    }

    // TodoItem 알람 시간 수정
    public TodoItemDto updateAlarmTime(Long todoItemId, TodoAlarmUpdateDto dto, Long userId){
        TodoItem todoItem = getTodoItem(todoItemId);
        validateOwnership(userId, todoItem);

        LocalTime alarmTime = null;
        if (dto.getHour() != null && dto.getMinute() != null) {
            alarmTime = LocalTime.of(dto.getHour(), dto.getMinute());
        }
        todoItem.updateAlarmTime(alarmTime);

        return TodoItemDto.from(todoItem);
    }

    // TodoItem 상태 변경
    public TodoItemDto toggleComplete(Long todoItemId, Long userId){
        TodoItem todoItem = getTodoItem(todoItemId);
        validateOwnership(userId, todoItem);
        todoItem.toggleCompletion();
        return TodoItemDto.from(todoItem);
    }

    // TodoItem 삭제
    public void removeTodoItem(Long todoItemId, Long userId){
        TodoItem todoItem = getTodoItem(todoItemId);
        validateOwnership(userId, todoItem);
        todoItem.remove(); // 연관관계 정리
        todoItemRepository.delete(todoItem);
    }

    private TodoItem createTodo(TodoItemRequestDto dto){
        return TodoItem.builder()
                .title(dto.getTitle())
                .isCompleted(false)
                .build();
    }

    private TodoItem createRoutineTodo(TodoItemRequestDto dto, LocalTime alarmTime, Long routineId) {
        return TodoItem.builder()
                .title(dto.getTitle())
                .isCompleted(false)
                .alarmTime(alarmTime)
                .routineId(routineId)
                .build();
    }

    private TodoItem getTodoItem(Long todoItemId) {
        return todoItemRepository.findByIdWithDailyTaskAndUser(todoItemId)
                .orElseThrow(() -> new NotFoundException("해당 일정이 존재하지 않습니다."));
    }

    private void validateOwnership(Long userId, TodoItem todoItem) {
        if (!todoItem.getDailyTask().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }
}
