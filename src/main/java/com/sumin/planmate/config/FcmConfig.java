package com.sumin.planmate.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class FcmConfig {

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("planmate-fcm-firebase-service-key.json");
            InputStream serviceAccount = resource.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if(FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);
                log.info("FCM 설정 성공");
            }
        } catch (IOException e) {
            log.error("FCM 초기화 오류: {}", e.getMessage());
        }
    }
}
