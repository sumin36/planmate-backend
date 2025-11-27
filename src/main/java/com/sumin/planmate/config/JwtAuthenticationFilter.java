package com.sumin.planmate.config;

import com.sumin.planmate.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // 토큰이 없거나 형식이 안맞으면 인증 실패
        if(header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        // 토큰 형식 및 유효성 검사
        if(jwtUtil.isInValidToken(token)) {
            throw new AuthenticationServiceException("Invalid JWT token");
        }

        // 토큰에서 로그인 id, role 추출
        String loginId = jwtUtil.getLoginId(token);
        String role = Jwts.parserBuilder()
                .setSigningKey(jwtUtil.getSigningKey())
                .build()
                .parseClaimsJws(token).getBody()
                .get("role").toString();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                loginId,
                null,
                List.of(new SimpleGrantedAuthority(role))
        );

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/api/auth");
    }
}
