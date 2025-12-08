package com.sumin.planmate.service;

import com.sumin.planmate.dto.dailytask.TodoItemRequestDto;
import com.sumin.planmate.dto.routine.RoutineDto;
import com.sumin.planmate.dto.routine.RoutineRequestDto;
import com.sumin.planmate.dto.routine.RoutineUpdateDto;
import com.sumin.planmate.entity.*;
import com.sumin.planmate.exception.NotFoundException;
import com.sumin.planmate.repository.DailyTaskRepository;
import com.sumin.planmate.repository.RoutineRepository;
import com.sumin.planmate.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final DailyTaskService dailyTaskService;
    private final DailyTaskRepository dailyTaskRepository;
    private final TodoItemRepository todoItemRepository;
    private final UserService userService;

    // 루틴 추가
    public void addRoutine(RoutineRequestDto dto, Long userId) {
        User user = userService.getUser(userId);
        Routine routine = createRoutine(dto);
        user.addRoutine(routine);

        Map<LocalDate, DailyTask> taskMap = getExistingDailyTaskMap(userId, routine.getStartDate(), routine.getEndDate());

        createTodoItemsForRoutine(user, routine, taskMap);
    }

    // 루틴 리스트 조회
    @Transactional(readOnly = true)
    public List<RoutineDto> getRoutines(Long userId) {
        List<Routine> routines = routineRepository.findByUserId(userId);
        return routines.stream().map(this::toDto).toList();
    }

    // 루틴 수정
    public RoutineDto updateRoutine(Long routineId, RoutineUpdateDto dto, Long userId) {
        Routine routine = getRoutine(routineId);
        validateOwnership(userId, routine);
        todoItemRepository.deleteByRoutineId(routineId); // 기존 루틴 기반 TodoItem 삭제

        routine.updateRoutine(
                dto.getTitle(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getRepeatType(),
                dto.getRepeatDescription(),
                dto.getHour(),
                dto.getMinute()
        );

        Map<LocalDate, DailyTask> taskMap = getExistingDailyTaskMap(userId, routine.getStartDate(), routine.getEndDate());

        createTodoItemsForRoutine(routine.getUser(), routine, taskMap);
        return toDto(routine);
    }

    // 루틴 삭제
    public void deleteRoutine(Long routineId, Long userId) {
        Routine routine = getRoutine(routineId);
        validateOwnership(userId, routine);
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
                .build();

        // 검증
        routine.validateDates();
        routine.validateRepeat();
        return routine;
    }

    private Map<LocalDate, DailyTask> getExistingDailyTaskMap(Long userId, LocalDate startDate, LocalDate endDate) {
        List<DailyTask> tasks = dailyTaskRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        return tasks.stream().collect(Collectors.toMap(DailyTask::getDate, Function.identity()));
    }

    // 루틴 설정 기간에 맞게 데일리 태스크에 TodoItem 추가
    private void createTodoItemsForRoutine(User user, Routine routine, Map<LocalDate, DailyTask> taskMap) {
        LocalDate date = routine.getStartDate();
        while (!date.isAfter(routine.getEndDate())) {
            if (shouldCreateDailyTask(routine, date)) {
                dailyTaskService.addTodoItemForRoutine(
                        (TodoItemRequestDto.create(date, routine.getTitle())),
                        routine.getAlarmTime(),
                        routine.getId(),
                        user,
                        taskMap);
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

    private Routine getRoutine(Long routineId) {
        return routineRepository.findById(routineId)
                .orElseThrow(() -> new NotFoundException("해당 루틴이 존재하지 않습니다."));
    }

    private void validateOwnership(Long userId, Routine routine) {
        if(!routine.getUser().getId().equals(userId)){
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
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
                .build();
    }
}
