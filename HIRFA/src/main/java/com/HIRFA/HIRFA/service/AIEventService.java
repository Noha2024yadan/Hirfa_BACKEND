package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.repository.AiUserEventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIEventService {

    private final AiUserEventsRepository events;

    public enum EventType { view, add_to_cart, purchase, like }

    private static final double W_VIEW = 0.2;
    private static final double W_CART = 0.7;
    private static final double W_PURCHASE = 1.0;
    private static final double W_LIKE = 0.6;

    private void log(String userId, String productId, EventType type, double weight, Map<String,Object> meta) {
        events.insertEvent(userId, productId, type.name(), weight, OffsetDateTime.now(), meta);
    }

    public void logView(String userId, String productId, Map<String,Object> meta) {
        log(userId, productId, EventType.view, W_VIEW, meta);
    }

    public void logAddToCart(String userId, String productId, int qty, Map<String,Object> meta) {
        if (meta != null) meta.put("qty", qty);
        log(userId, productId, EventType.add_to_cart, W_CART, meta);
    }

    public void logPurchase(String userId, String productId, int qty, double price, Map<String,Object> meta) {
        if (meta != null) { meta.put("qty", qty); meta.put("price", price); }
        log(userId, productId, EventType.purchase, W_PURCHASE, meta);
    }

    public void logLike(String userId, String productId, Map<String,Object> meta) {
        log(userId, productId, EventType.like, W_LIKE, meta);
    }
}
