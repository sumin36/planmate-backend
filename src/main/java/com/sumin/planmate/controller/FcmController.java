package com.sumin.planmate.controller;

import com.sumin.planmate.dto.FcmTokenRequestDto;
import com.sumin.planmate.dto.user.CustomUserDetails;
import com.sumin.planmate.service.FcmService;
import com.sumin.planmate.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/token")
    public ApiResponse<String> saveToken(@RequestBody FcmTokenRequestDto dto,
                                         @AuthenticationPrincipal CustomUserDetails userDetails){
        fcmService.saveToken(userDetails.getUserId(), dto.getToken());
        return new ApiResponse<>(200, "FCM 토큰 저장 완료", null);
    }
}
