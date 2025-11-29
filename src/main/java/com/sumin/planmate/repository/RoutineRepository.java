package com.sumin.planmate.repository;

import com.sumin.planmate.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByUserLoginId(String loginId);
}
