package com.sumin.planmate.service;

import com.sumin.planmate.dto.dailytask.DailyTaskDto;
import com.sumin.planmate.dto.dailytask.DailyTaskRequestDto;
import com.sumin.planmate.entity.DailyTask;
import com.sumin.planmate.entity.User;
import com.sumin.planmate.exception.NotFoundException;
import com.sumin.planmate.repository.DailyTaskRepository;
import com.sumin.planmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyTaskService {

    private final UserRepository userRepository;
    private final DailyTaskRepository dailyTaskRepository;

    // Todo 추가
    public void addDailyTask(String loginId, DailyTaskRequestDto dto){
        User user = getUser(loginId);
        DailyTask task = DailyTask.builder().title(dto.getTitle())
                .description(dto.getDescription())
                .date(dto.getDate())
                .isCompleted(false)
                .build();
        user.addDailyTask(task);
    }

    // 날짜별 Todo 조회
    @Transactional(readOnly = true)
    public List<DailyTaskDto> getDailyTasksByDate(String loginId, LocalDate date){
        User user = getUser(loginId);
        List<DailyTask> tasks = dailyTaskRepository.findByUser_LoginIdAndDate(user.getLoginId(), date);
        return tasks.stream().map(this::toDto).toList();
    }

    // Todo 상태 변경(완료, 취소)
    public void toggleComplete(Long taskId){
        DailyTask task = getDailyTask(taskId);
        task.setComplete(task.getIsCompleted());
    }

    // Todo 삭제
    public void removeDailyTask(Long taskId){
        DailyTask task = getDailyTask(taskId);
        dailyTaskRepository.delete(task);
    }

    // Todo 수정
    public DailyTaskDto updateDailyTask(Long taskId, DailyTaskRequestDto dto){
        DailyTask task = getDailyTask(taskId);
        return toDto(task.update(dto.getTitle(), dto.getDescription(), dto.getDate()));
    }

    private User getUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }

    private DailyTask getDailyTask(Long taskId) {
        return dailyTaskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("일정이 존재하지 않습니다."));
    }

    private DailyTaskDto toDto(DailyTask dailyTask) {
        return DailyTaskDto.builder()
                .taskId(dailyTask.getId())
                .title(dailyTask.getTitle())
                .description(dailyTask.getDescription())
                .date(dailyTask.getDate())
                .isCompleted(dailyTask.getIsCompleted())
                .build();
    }
}
