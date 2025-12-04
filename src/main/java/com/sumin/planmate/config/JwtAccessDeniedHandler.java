package com.sumin.planmate.config;

import com.sumin.planmate.util.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * 사용자 인증은 되었지만 권한(role)이 없을 경우(403) 호출 -> 인가 실패
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       @NonNull AccessDeniedException accessDeniedException) throws IOException {

        ErrorResponse error = ErrorResponse.generalError(
                403,
                "FORBIDDEN",
                "접근 권한이 없습니다.",
                request.getRequestURI()
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");

        new ObjectMapper().writeValue(response.getWriter(), error);
    }
}
