package com.sumin.planmate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DailyTask extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dailytask_id")
    private Long id;

    @NotBlank
    private String title;
    private String description;

    @NotNull
    private LocalDate date;
    private Boolean isCompleted;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    public void setComplete(boolean complete) {
        this.isCompleted = !complete;
    }

    public DailyTask update(String title, String description, LocalDate date) {
        if(title != null) this.title = title;
        if(description != null) this.description = description;
        if(date != null) this.date = date;
        return this;
    }
}
