package com.sumin.planmate.service;

import com.sumin.planmate.dto.dailytask.DailyTaskDto;
import com.sumin.planmate.dto.dailytask.TodoItemRequestDto;
import com.sumin.planmate.dto.dailytask.TodoItemUpdateDto;
import com.sumin.planmate.dto.dailytask.TodoItemDto;
import com.sumin.planmate.entity.DailyTask;
import com.sumin.planmate.entity.TodoItem;
import com.sumin.planmate.entity.User;
import com.sumin.planmate.exception.NotFoundException;
import com.sumin.planmate.repository.DailyTaskRepository;
import com.sumin.planmate.repository.TodoItemRepository;
import com.sumin.planmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyTaskService {

    private final UserRepository userRepository;
    private final DailyTaskRepository dailyTaskRepository;
    private final TodoItemRepository todoItemRepository;

    // TodoItem 추가
    public TodoItemDto addTodoItem(TodoItemRequestDto dto, Long routineId, Long userId){
        User user = getUser(userId);
        DailyTask dailyTask = dailyTaskRepository.findByUserIdAndDate(userId, dto.getDate())
                .orElseGet(() -> {
                    DailyTask newTask = DailyTask.builder()
                            .date(dto.getDate())
                            .build();
                    user.addDailyTask(newTask);
                    return newTask;
                });

        TodoItem todoItem = TodoItem.builder()
                .title(dto.getTitle())
                .isCompleted(false)
                .routineId(routineId)
                .build();

        dailyTask.addTodoItem(todoItem);
        return toDto(todoItem);
    }

    // 날짜별 TodoItem 리스트 조회
    @Transactional(readOnly = true)
    public DailyTaskDto getDailyTasksByDate(LocalDate date, Long userId){
        DailyTask task = dailyTaskRepository.findByUserIdAndDate(userId, date)
                .orElse(DailyTask.builder().date(date).build());
        return toDto(task);
    }

    // TodoItem 수정
    public TodoItemDto updateDailyTask(Long todoItemId, TodoItemUpdateDto dto, Long userId){
        TodoItem todoItem = getTodoItem(todoItemId);
        validateOwnership(userId, todoItem);
        TodoItem updated = todoItem.update(dto.getTitle(), dto.getMemo());
        return toDto(updated);
    }

    // TodoItem 상태 변경
    public TodoItemDto toggleComplete(Long todoItemId, Long userId){
        TodoItem todoItem = getTodoItem(todoItemId);
        validateOwnership(userId, todoItem);
        todoItem.setComplete(!todoItem.getIsCompleted());
        return toDto(todoItem);
    }

    // TodoItem 삭제
    public void removeTodoItem(Long todoItemId, Long userId){
        TodoItem todoItem = getTodoItem(todoItemId);
        validateOwnership(userId, todoItem);
        todoItem.remove(); // 연관관계 정리
        todoItemRepository.deleteById(todoItem.getId());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }

    private TodoItem getTodoItem(Long todoItemId) {
        return todoItemRepository.findByIdWithDailyTaskAndUser(todoItemId)
                .orElseThrow(() -> new NotFoundException("해당 일정이 존재하지 않습니다."));
    }

    private void validateOwnership(Long userId, TodoItem todoItem) {
        if(!todoItem.getDailyTask().getUser().getId().equals(userId)){
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }

    private DailyTaskDto toDto(DailyTask dailyTask) {
        return DailyTaskDto.create(
                dailyTask.getDate(),
                dailyTask.getTodoItems().stream()
                        .map(this::toDto)
                        .toList()
        );
    }

    private TodoItemDto toDto(TodoItem todoItem){
        return TodoItemDto.builder()
                .id(todoItem.getId())
                .title(todoItem.getTitle())
                .memo(todoItem.getMemo())
                .isCompleted(todoItem.getIsCompleted())
                .routineId(todoItem.getRoutineId())
                .build();
    }
}
