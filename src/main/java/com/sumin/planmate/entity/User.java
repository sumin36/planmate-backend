package com.sumin.planmate.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DailyTask> dailyTasks = new ArrayList<>();

    public User update(String nickname, String email, Gender gender, LocalDate birthDate) {
        if (nickname != null) this.nickname = nickname;
        if (email != null) this.email = email;
        if (gender != null) this.gender = gender;
        if (birthDate != null) this.birthDate = birthDate;
        return this;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void addDailyTask(DailyTask dailyTask) {
        this.dailyTasks.add(dailyTask);
        dailyTask.setUser(this);
    }
}
