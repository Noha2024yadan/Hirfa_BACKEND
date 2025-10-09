package com.HIRFA.HIRFA.config;

import com.HIRFA.HIRFA.web.ProductViewInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final ProductViewInterceptor productViewInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(productViewInterceptor)
                .addPathPatterns("/products/**");
    }
}
