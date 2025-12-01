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
    public TodoItemDto addTodoItem(String loginId, TodoItemRequestDto dto){
        User user = getUser(loginId);
        DailyTask dailyTask = dailyTaskRepository.findByUserLoginIdAndDate(loginId, dto.getDate())
                .orElseGet(() -> {
                    DailyTask newTask = DailyTask.builder()
                            .date(dto.getDate())
                            .user(user)
                            .build();
                    user.addDailyTask(newTask);
                    return newTask;
                });

        TodoItem todoItem = TodoItem.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .isCompleted(false)
                .build();

        dailyTask.addTodoItem(todoItem);
        return toDto(todoItem);
    }

    // 날짜별 TodoItem 리스트 조회
    @Transactional(readOnly = true)
    public DailyTaskDto getDailyTasksByDate(String loginId, LocalDate date){
        DailyTask task = dailyTaskRepository.findByUserLoginIdAndDate(loginId, date)
                .orElse(DailyTask.builder().date(date).build());
        return toDto(task);
    }

    // TodoItem 수정
    public TodoItemDto updateDailyTask(Long itemId, TodoItemUpdateDto dto){
        TodoItem todoItem = getTodoItem(itemId);
        TodoItem updated = todoItem.update(dto.getTitle(), dto.getDescription());
        return toDto(updated);
    }

    // TodoItem 상태 변경
    public TodoItemDto toggleComplete(Long itemId){
        TodoItem todoItem = getTodoItem(itemId);
        todoItem.setComplete(!todoItem.getIsCompleted());
        return toDto(todoItem);
    }

    // TodoItem 삭제
    public void removeTodoItem(Long itemId){
        TodoItem todoItem = getTodoItem(itemId);
        todoItem.remove();
    }

    private User getUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }

    private TodoItem getTodoItem(Long itemId) {
        return todoItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("해당 일정이 존재하지 않습니다."));
    }

    // Todo: N + 1 문제 해결 하기
    private DailyTaskDto toDto(DailyTask dailyTask) {
        return DailyTaskDto.builder()
                .date(dailyTask.getDate())
                .todoItems(dailyTask.getTodoItems().stream()
                        .map(this::toDto)
                        .toList())
                .build();
    }

    private TodoItemDto toDto(TodoItem todoItem){
        return TodoItemDto.builder()
                .id(todoItem.getId())
                .title(todoItem.getTitle())
                .description(todoItem.getDescription())
                .isCompleted(todoItem.getIsCompleted())
                .build();
    }
}
