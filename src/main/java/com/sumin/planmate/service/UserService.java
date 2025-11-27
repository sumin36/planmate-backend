package com.sumin.planmate.service;

import com.sumin.planmate.dto.user.*;
import com.sumin.planmate.entity.User;
import com.sumin.planmate.exception.NotFoundException;
import com.sumin.planmate.exception.UnauthorizedException;
import com.sumin.planmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 정보 수정
    public void updateUserInfo(String loginId, UserUpdateDto dto){
        User user = getUser(loginId);
        user.changeUserInfo(dto.getNickname(), dto.getEmail(), dto.getGender(), dto.getBirthDate());
    }

    // 유저 정보 조회
    @Transactional(readOnly = true)
    public UserInfoDto getUserInfo(String loginId){
        User user = getUser(loginId);

        return UserInfoDto.builder()
                .loginId(loginId)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .build();
    }

    // 비밀번호 변경
    public void changePassword(String loginId, PasswordChangeDto dto){
        User user = getUser(loginId);

        if(!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())){
            throw new UnauthorizedException("현재 비밀번호가 일치하지 않습니다.");
        }
        user.changePassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    private User getUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }
}
