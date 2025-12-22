package com.sumin.planmate.controller;

import com.sumin.planmate.util.ApiResponse;
import com.sumin.planmate.dto.user.*;
import com.sumin.planmate.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    public final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ApiResponse<Void> signup(@Valid @RequestBody UserSignupDto dto){
        authService.signup(dto);
        return new ApiResponse<>(200, "회원가입 성공", null);
    }

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 확인하여 AccessToken(30분)과 RefreshToken(7일)을 발급합니다.")
    @PostMapping("/login")
    public ApiResponse<UserLoginResponseDto> login(@RequestBody UserLoginDto dto) {
        UserLoginResponseDto response = authService.login(dto);
        return new ApiResponse<>(200, "로그인 성공", response);
    }

    @Operation(summary = "토큰 재발급", description = "만료된 AccessToken 대신 유효한 RefreshToken을 사용하여 새로운 AccessToken을 발급받습니다.")
    @PostMapping("/token/reissue")
    public ApiResponse<UserLoginResponseDto> reissue(@RequestParam String refreshToken){
        UserLoginResponseDto response = authService.reissueAccessToken(refreshToken);
        return new ApiResponse<>(200, "토큰 재발급 성공", response);
    }
}
