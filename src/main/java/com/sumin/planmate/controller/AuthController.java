package com.sumin.planmate.controller;

import com.sumin.planmate.util.ApiResponse;
import com.sumin.planmate.dto.user.*;
import com.sumin.planmate.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    public final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<UserSignUpResponseDto> signup(@Valid @RequestBody UserSignupDto dto){
        UserSignUpResponseDto response = authService.signup(dto);
        return new ApiResponse<>(200, "회원가입 성공", response);
    }

    @PostMapping("/login")
    public ApiResponse<UserLoginResponseDto> login(@RequestBody UserLoginDto dto) {
        UserLoginResponseDto response = authService.login(dto);
        return new ApiResponse<>(200, "로그인 성공", response);
    }

    @PostMapping("/token/reissue")
    public ApiResponse<String> reissue(@RequestParam String refreshToken){
        String newAccessToken = authService.reissueAccessToken(refreshToken);
        return new ApiResponse<>(200, "토큰 재발급 성공", newAccessToken);
    }
}
