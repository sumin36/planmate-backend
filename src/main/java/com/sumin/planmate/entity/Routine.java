package com.sumin.planmate.entity;

import com.sumin.planmate.exception.InvalidRoutineException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Routine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RepeatType repeatType = RepeatType.DAILY;
    private String repeatDescription;
    private LocalTime alarmTime;
    private boolean isActive = true;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    public Routine updateRoutine (String title, LocalDate startDate, LocalDate endDate, RepeatType repeatType,
                   String repeatDescription, Integer hour, Integer minute) {
        if(title != null) this.title = title;
        if(startDate != null) this.startDate = startDate;
        if(endDate != null) this.endDate = endDate;
        if (repeatType != null) {
            this.repeatType = repeatType;
            if (repeatType == RepeatType.DAILY) this.repeatDescription = null;
            else if (repeatDescription != null) this.repeatDescription = repeatDescription;
        }

        if(hour != null && minute != null) this.alarmTime = LocalTime.of(hour, minute);

        validateDates();
        validateRepeat();

        return this;
    }

    public void validateDates() {
        if(this.startDate.isAfter(this.endDate)) {
            throw new InvalidRoutineException("시작 날짜는 종료 날짜보다 앞서야 합니다.");
        }
    }

    public void validateRepeat() {
        if(this.repeatType != RepeatType.DAILY
                && (this.repeatDescription == null || this.repeatDescription.isBlank())) {
            throw new InvalidRoutineException("반복 타입이 매일(DAILY)이 아닌 경우 repeatDescription 필수입니다.");
        }
    }
}
