package com.sumin.planmate.controller;

import com.sumin.planmate.util.ApiResponse;
import com.sumin.planmate.dto.user.*;
import com.sumin.planmate.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @GetMapping
    public ApiResponse<UserInfoDto> getUserInfo(@AuthenticationPrincipal String loginId) {
        UserInfoDto dto = userService.getUserInfo(loginId);
        return new ApiResponse<>(200, "조회 성공", dto);
    }

    @PutMapping
    public ApiResponse<String> updateUser(@AuthenticationPrincipal String loginId,
                                             @RequestBody UserUpdateDto dto) {
        userService.updateUserInfo(loginId, dto);
        return new ApiResponse<>(200, "정보 수정 완료", null);
    }

    @PatchMapping("/password")
    public ApiResponse<String> changePassword(@AuthenticationPrincipal String loginId,
                                              @Valid @RequestBody PasswordChangeDto dto) {
        userService.changePassword(loginId, dto);
        return new ApiResponse<>(200, "비빌번호 변경 완료", null);
    }
}
