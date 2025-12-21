package com.sumin.planmate.repository;

import com.sumin.planmate.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Diary findByUserIdAndDate(Long userId, LocalDate date);
}
