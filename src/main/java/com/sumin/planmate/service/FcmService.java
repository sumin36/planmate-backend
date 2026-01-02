package com.sumin.planmate.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.sumin.planmate.entity.FcmToken;
import com.sumin.planmate.entity.User;
import com.sumin.planmate.repository.FcmTokenRepository;
import com.sumin.planmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmService {

    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;

    public void sendNotificationToUser(Long userid, String title, String body) {
        List<FcmToken> tokens = fcmTokenRepository.findByUserId(userid);

        for (FcmToken token : tokens) {
            sendNotification(token.getToken(), title, body);
        }
    }

    private void sendNotification(String token, String title, String body) {

        // 전송할 내용
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body).
                        build())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 전송 실패: {}", e.getMessage());
        }
    }

    @Transactional
    public void saveToken(Long userId, String token) {
        boolean exists = fcmTokenRepository.existsByUserIdAndToken(userId, token);

        if (!exists) {
            User user = userRepository.getReferenceById(userId);
            fcmTokenRepository.save(FcmToken.create(user, token));
        }
    }
}
