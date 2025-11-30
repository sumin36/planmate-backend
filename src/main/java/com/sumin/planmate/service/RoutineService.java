package com.sumin.planmate.service;

import com.sumin.planmate.dto.routine.RoutineDto;
import com.sumin.planmate.dto.routine.RoutineRequestDto;
import com.sumin.planmate.dto.routine.RoutineUpdateDto;
import com.sumin.planmate.entity.RepeatType;
import com.sumin.planmate.entity.Routine;
import com.sumin.planmate.entity.User;
import com.sumin.planmate.exception.NotFoundException;
import com.sumin.planmate.repository.RoutineRepository;
import com.sumin.planmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineService {

    private final UserRepository userRepository;
    private final RoutineRepository routineRepository;

    // 루틴 추가
    public void addRoutine(String loginId, RoutineRequestDto dto) {
        User user = getUser(loginId);

        Integer hour = dto.getHour();
        Integer minute = dto.getMinute();

        Routine routine = Routine.builder()
                .title(dto.getTitle())
                .endDate(dto.getEndDate())
                .startDate(dto.getStartDate())
                .repeatType(dto.getRepeatType())
                .repeatDescription(dto.getRepeatType() == RepeatType.DAILY ? null : dto.getRepeatDescription())
                .alarmTime(hour != null && minute != null ? LocalTime.of(hour, minute) : null)
                .isActive(true)
                .build();

        // 검증
        routine.validateDates();
        routine.validateRepeat();

        user.addRoutine(routine);
    }

    // 루틴 조회
    @Transactional(readOnly = true)
    public List<RoutineDto> getRoutines(String loginId) {
        List<Routine> routines = routineRepository.findByUserLoginId(loginId);
        return routines.stream().map(this::toDto).toList();
    }

    // 루틴 수정
    public RoutineDto updateRoutine(Long routineId, RoutineUpdateDto dto) {
        Routine routine = getRoutine(routineId);
        Routine updated = routine.updateRoutine(dto.getTitle(), dto.getStartDate(), dto.getEndDate(),
                dto.getRepeatType(), dto.getRepeatDescription(), dto.getHour(), dto.getMinute());
        return toDto(updated);
    }

    // 루틴 상태 변경(켜기, 끄기)
    public void toggleActive(Long routineId) {
        Routine routine = getRoutine(routineId);
        routine.setActive(routine.isActive());
    }

    // 루틴 삭제
    public void deleteRoutine(Long routineId) {
        Routine routine = getRoutine(routineId);
        routineRepository.delete(routine);
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
