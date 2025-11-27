package com.sumin.planmate.service;

import com.sumin.planmate.dto.user.*;
import com.sumin.planmate.entity.User;
import com.sumin.planmate.exception.ConflictException;
import com.sumin.planmate.exception.NotFoundException;
import com.sumin.planmate.exception.UnauthorizedException;
import com.sumin.planmate.repository.UserRepository;
import com.sumin.planmate.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    public UserSignUpResponseDto signup(UserSignupDto dto){
        if(userRepository.existsByLoginId(dto.getLoginId())) {
            throw new ConflictException("이미 존재하는 아이디입니다.");
        }
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .build();

        userRepository.save(user);
        return UserSignUpResponseDto.toDto(user);
    }

    // 로그인
    @Transactional(readOnly = true)
    public UserLoginResponseDto login(UserLoginDto dto){
        User user = userRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new NotFoundException("아이디 또는 비밀번호가 틀립니다"));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("아이디 또는 비밀번호가 틀립니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getLoginId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getLoginId());

        return UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(user.getNickname())
                .build();
    }

    // Refresh Token 사용해서 Access Token 재발급
    @Transactional(readOnly = true)
    public String reissueAccessToken(String refreshToken){
        if(jwtUtil.isInValidToken(refreshToken)) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
        String loginId = jwtUtil.getLoginId(refreshToken);
        return jwtUtil.generateAccessToken(loginId);
    }
}
