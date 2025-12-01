package com.sumin.planmate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DailyTask extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_task_id")
    private Long id;

    @NotNull
    private LocalDate date;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "dailyTask", cascade = CascadeType.ALL)
    private List<TodoItem> todoItems = new ArrayList<>();

    public void addTodoItem(TodoItem todoItem){
        this.todoItems.add(todoItem);
        if(todoItem.getDailyTask() != this) {
            todoItem.setDailyTask(this);
        }
    }
}
