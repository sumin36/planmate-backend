package com.sumin.planmate.repository;

import com.sumin.planmate.entity.Routine;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user"})
    Optional<Routine> findById(@NonNull Long routineId);

    List<Routine> findByUserId(Long userId);

}
