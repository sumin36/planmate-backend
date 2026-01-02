package com.sumin.planmate.scheduler;

import com.sumin.planmate.entity.TodoItem;
import com.sumin.planmate.repository.TodoItemRepository;
import com.sumin.planmate.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AlarmScheduler {

    private final TodoItemRepository todoItemRepository;
    private final FcmService fcmService;

    @Scheduled(cron = "0 * * * * *") // 매분 0초마다 실행
    public void checkAndSendAlarm(){
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        List<TodoItem> items = todoItemRepository.findByDateAndAlarmTimeWithUser(today, now);

        for (TodoItem item : items) {
            fcmService.sendNotificationToUser(
                    item.getDailyTask().getUser().getId(),
                    "planmate 알림",
                    item.getTitle());
        }
    }
}