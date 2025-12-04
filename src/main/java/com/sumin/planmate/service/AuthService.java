package com.sumin.planmate.service;

import com.sumin.planmate.dto.user.*;
import com.sumin.planmate.entity.User;
import com.sumin.planmate.exception.ConflictException;
import com.sumin.planmate.exception.UnauthorizedException;
import com.sumin.planmate.repository.UserRepository;
import com.sumin.planmate.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private final AuthenticationManager authenticationManager;

    // 회원가입
    public void signup(UserSignupDto dto){
        if(userRepository.existsByUsername(dto.getUsername())) {
            throw new ConflictException("이미 존재하는 아이디입니다.");
        }
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("ROLE_USER")
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .build();

        userRepository.save(user);
    }

    // 로그인
    public UserLoginResponseDto login(UserLoginDto dto){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            String username = authentication.getName();
            User user = getUser(username);
            return issueToken(username, user); // 토큰 발급
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("아이디 또는 비밀번호가 틀립니다.");
        }
    }

    // 토큰 재발급
    public UserLoginResponseDto reissueAccessToken(String refreshToken){
        if(jwtUtil.isInValidToken(refreshToken)) { // 토큰 유효성 확인
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");
        }

        String username = jwtUtil.getLoginId(refreshToken);
        User user = getUser(username); //유저 존재 확인
        if(!user.getRefreshToken().equals(refreshToken)) {
            throw new UnauthorizedException("토큰이 일치하지 않습니다.");
        }
        return issueToken(username, user);
    }

    private UserLoginResponseDto issueToken(String username, User user) {
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);
        user.updateRefreshToken(refreshToken);

        return UserLoginResponseDto.builder()
                .username(username)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .fullName(user.getFullName())
                .build();
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("존재하지 않는 사용자입니다."));
    }
}
