package com.HIRFA.HIRFA.web;

import com.HIRFA.HIRFA.service.AIEventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ProductViewInterceptor implements HandlerInterceptor {

    private final AIEventService ai;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        String uri = req.getRequestURI();
        if (req.getMethod().equals("GET") && uri.matches("^/products/[^/]+$")) {
            String productId = uri.substring(uri.lastIndexOf('/') + 1);
            String userId = resolveUserId(req);
            Map<String,Object> meta = new HashMap<>();
            meta.put("ua", req.getHeader("User-Agent"));
            ai.logView(userId, productId, meta);
        }
        return true;
    }

    private String resolveUserId(HttpServletRequest req) {
        String uid = req.getHeader("X-User-Id");
        return (uid != null && !uid.isBlank()) ? uid : "anonymous-" + UUID.randomUUID();
    }
}
