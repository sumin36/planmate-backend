package com.sumin.planmate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TodoItem extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;
    private String memo;
    private Boolean isCompleted;
    private Long routineId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_task_id")
    private DailyTask dailyTask;

    public TodoItem update(String title, String memo) {
        if(title != null) this.title = title;
        if(memo != null) this.memo = memo;
        return this;
    }

    public void setComplete(boolean complete) {
        this.isCompleted = complete;
    }

    public void remove(){
        if (this.dailyTask != null) {
            this.dailyTask.getTodoItems().remove(this);
            this.dailyTask = null;
        }
    }
}
