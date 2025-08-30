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
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        String token = request.getHeader("X-Auth-Token");
        if (token == null || token.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        Long userId = authService.authenticate(token);
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        request.setAttribute("userId", userId);
        return true;
    }
}
