package io.github.myeongseokkang.harusaegim.config;

import io.github.myeongseokkang.harusaegim.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenAuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    public TokenAuthInterceptor(AuthService authService) { this.authService = authService; }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //preflight 제외
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        //토큰 없음
        String token = request.getHeader("X-Auth-Token");
        if (token == null || token.isBlank()) return unauthorized(response);

        //인증 실패(null)
        Long userId = authService.authenticate(token);
        if (userId == null) return unauthorized(response);

        request.setAttribute("userId", userId);
        return true;
    }

    private boolean unauthorized(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
        return false;
    }
}
