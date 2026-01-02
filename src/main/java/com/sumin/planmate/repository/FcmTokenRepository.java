package com.sumin.planmate.repository;

import com.sumin.planmate.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    List<FcmToken> findByUserId(Long userId);
    boolean existsByUserIdAndToken(Long userId, String token);
}
