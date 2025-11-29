package com.sumin.planmate.service;

import com.sumin.planmate.dto.routine.RoutineDto;
import com.sumin.planmate.dto.routine.RoutineRequestDto;
import com.sumin.planmate.entity.RepeatType;
import com.sumin.planmate.entity.Routine;
import com.sumin.planmate.entity.User;
import com.sumin.planmate.exception.InvalidRoutineException;
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

        if(dto.getRepeatType() != RepeatType.DAILY){
            if(dto.getRepeatDescription() == null || dto.getRepeatDescription().isBlank()){
                throw new InvalidRoutineException("반복 타입이 매일(DAILY)이 아닌 경우 repeatDescription 필수입니다.");
            }
        }

        LocalTime alarmTime = null;
        if(dto.getHour() != null && dto.getMinute() != null) {
            alarmTime = LocalTime.of(dto.getHour(), dto.getMinute(), 0);
        }

        Routine routine = Routine.builder()
                .title(dto.getTitle())
                .endDate(dto.getEndDate())
                .startDate(dto.getStartDate())
                .repeatType(dto.getRepeatType())
                .repeatDescription(dto.getRepeatType() == RepeatType.DAILY ? null : dto.getRepeatDescription())
                .alarmTime(alarmTime)
                .isActive(true)
                .build();

        user.addRoutine(routine);
    }

    // 루틴 조회
    @Transactional(readOnly = true)
    public List<RoutineDto> getRoutines(String loginId) {
        List<Routine> routines = routineRepository.findByUserLoginId(loginId);
        return routines.stream().map(this::toDto).toList();
    }

    // 루틴 수정

    // 루틴 켜기 or 끄기

    // 루틴 삭제

    private User getUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }

    private RoutineDto toDto(Routine routine) {
        return RoutineDto.builder()
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
