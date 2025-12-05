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

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // 유저 정보 수정
    public UserInfoDto updateUserInfo(UserUpdateDto dto, Long userId) {
        User user = getUser(userId);
        user.updateUserInfo(dto.getFullName(), dto.getEmail(), dto.getGender(), dto.getBirthDate());
        return toDto(user);
    }

    // 유저 정보 조회
    @Transactional(readOnly = true)
    public UserInfoDto getUserInfo(Long userId){
        User user = getUser(userId);
        return toDto(user);
    }

    // 비밀번호 변경
    public void changePassword(PasswordChangeDto dto, Long userId){
        User user = getUser(userId);
        if(!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())){
            throw new UnauthorizedException("현재 비밀번호가 일치하지 않습니다.");
        }
        user.changePassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }

    private UserInfoDto toDto(User user) {
        return UserInfoDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .build();
    }
}
