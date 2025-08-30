package io.github.myeongseokkang.harusaegim.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final TokenAuthInterceptor tokenAuthInterceptor;
    public WebConfig(TokenAuthInterceptor tokenAuthInterceptor) { this.tokenAuthInterceptor = tokenAuthInterceptor; }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenAuthInterceptor)
                .excludePathPatterns(
                        "/api/auth/**",
                        "/error",
                        "/h2-console/**"
                );
    }
}
