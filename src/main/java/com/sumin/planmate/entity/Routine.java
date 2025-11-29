package com.sumin.planmate.entity;

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
}
