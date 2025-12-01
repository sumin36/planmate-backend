package com.sumin.planmate.service;

import com.sumin.planmate.dto.dailytask.TodoItemRequestDto;
import com.sumin.planmate.dto.routine.RoutineDto;
import com.sumin.planmate.dto.routine.RoutineRequestDto;
import com.sumin.planmate.dto.routine.RoutineUpdateDto;
import com.sumin.planmate.entity.*;
import com.sumin.planmate.exception.NotFoundException;
import com.sumin.planmate.repository.RoutineRepository;
import com.sumin.planmate.repository.TodoItemRepository;
import com.sumin.planmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineService {

    private final UserRepository userRepository;
    private final RoutineRepository routineRepository;
    private final DailyTaskService dailyTaskService;
    private final TodoItemRepository todoItemRepository;

    // 루틴 추가
    public void addRoutine(String loginId, RoutineRequestDto dto) {
        User user = getUser(loginId);

        Routine routine = createRoutine(dto);
        user.addRoutine(routine);

        // 루틴 설정 기간에 해당하는 데일리 태스크에 TodoItem 추가
        createTodoItemsForRoutine(loginId, routine);
    }

    // 루틴 리스트 조회
    @Transactional(readOnly = true)
    public List<RoutineDto> getRoutines(String loginId) {
        List<Routine> routines = routineRepository.findByUserLoginId(loginId);
        return routines.stream().map(this::toDto).toList();
    }

    // 루틴 수정
    public RoutineDto updateRoutine(Long routineId, RoutineUpdateDto dto) {
        Routine routine = getRoutine(routineId);
        Routine updated = routine.updateRoutine(
                dto.getTitle(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getRepeatType(),
                dto.getRepeatDescription(),
                dto.getHour(),
                dto.getMinute()
        );

        List<TodoItem> items = todoItemRepository.findByRoutineId(routineId);
        removeInvalidTodoItems(items, updated); // 수정된 기간에 해당하지 않는 TodoItem 삭제
        // Todo: TodoItem 자체를 수정 추가하지 말고
        createTodoItemsForRoutine(updated.getUser().getLoginId(), updated); // 수정된 기간에 해당하는 TodoItem 추가
        return toDto(updated);
    }

    // 루틴 삭제
    public void deleteRoutine(Long routineId) {
        Routine routine = getRoutine(routineId);
        todoItemRepository.deleteFutureTodoItems(routineId, LocalDate.now());
        routineRepository.delete(routine);
    }

    // 루틴 생성
    private Routine createRoutine(RoutineRequestDto dto) {
        Routine routine = Routine.builder()
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .repeatType(dto.getRepeatType())
                .repeatDescription(dto.getRepeatType() == RepeatType.DAILY ? null : dto.getRepeatDescription())
                .alarmTime(dto.getHour() != null && dto.getMinute() != null ? LocalTime.of(dto.getHour(), dto.getMinute()) : null)
                .isActive(true)
                .build();

        // 검증
        routine.validateDates();
        routine.validateRepeat();
        return routine;
    }

    private void createTodoItemsForRoutine(String loginId, Routine routine) {
        LocalDate date = routine.getStartDate();
        while (!date.isAfter(routine.getEndDate())) {
            if (shouldCreateDailyTask(routine, date)) {
                dailyTaskService.addTodoItem(loginId,
                        TodoItemRequestDto.builder()
                                .title(routine.getTitle())
                                .date(date)
                                .build());
            }
            date = date.plusDays(1);
        }
    }

    private boolean shouldCreateDailyTask(Routine routine, LocalDate date) {
        switch (routine.getRepeatType()) {
            case DAILY:
                return true;
            case WEEKLY:
                List<String> dayOfWeeks = Arrays.stream(routine.getRepeatDescription().split(",")).toList();
                String day = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
                return dayOfWeeks.contains(day);
            case MONTHLY:
                List<Integer> days = Arrays.stream(routine.getRepeatDescription().split(","))
                        .map(Integer::parseInt)
                        .toList();
                return days.contains(date.getDayOfMonth());
            default:
                return false;
        }
    }

    private void removeInvalidTodoItems(List<TodoItem> items, Routine routine) {
        LocalDate start = routine.getStartDate();
        LocalDate end = routine.getEndDate();

        for (TodoItem item : items) {
            LocalDate date = item.getDailyTask().getDate();
            if(date.isBefore(start) || date.isAfter(end)) {
                todoItemRepository.delete(item);
            }
        }
    }

    private User getUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }

    private Routine getRoutine(Long routineId) {
        return routineRepository.findById(routineId)
                .orElseThrow(() -> new NotFoundException("해당 루틴이 존재하지 않습니다."));
    }

    private RoutineDto toDto(Routine routine) {
        return RoutineDto.builder()
                .routineId(routine.getId())
                .title(routine.getTitle())
                .startDate(routine.getStartDate())
                .endDate(routine.getEndDate())
                .repeatType(routine.getRepeatType())
                .repeatDescription(routine.getRepeatDescription())
                .alarmTime(routine.getAlarmTime())
                .isActive(routine.isActive())
                .build();
    }
}
