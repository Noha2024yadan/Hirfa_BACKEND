package com.HIRFA.HIRFA.services;

import org.springframework.beans.annotation.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
@Slf4j
@Service
public class AIEventService{
    @Autowired
    private JdbcTemplate JdbcTemplate;
    private static final Map<String, Double> EVENT_WEIGHTS= Map.of(
        "view", 0.2,
        "add_to_card", 0.7,
        "purchase", 1.0,
        "like", 0.5
    );
    @EnableAsync
    public void trackEvent(UUID clientId, UUID productId, String eventType){
        try{
            String sql = """
               INSERT INTO ai.user_events
               (client_id, product_id, event_type, weight, ts, infos_sup)
               VALUES (?, ?, ?, ?, NOW(), '{}' ::jsonb)
            """;
            double weight = EVENT_WEIGHTS.getOrDefault(eventType, 0.1);
            jdbcTemplate.update(sql, clientId, productId, eventType, weight);
            log.debug("Événement IA enregistré: {} | Client: {} | Product: {}",
                       eventType, clientId, productId);
        }catch(Exception e){
            log.error("Erreur lors de l'enregistrement événement IA: {}",
                      e.getMessage());
        }
    }
    @EnableAsync
    public void trackEventWithMeta(UUID clientId, UUID productId, String eventType, String metaJson){
        try{
            String sql = """
                INSERT INTO ai.user_events
                (client_id, product_id, event_type, weight, ts, infos_sup)
                VALUES (?, ?, ?, ?, NOW(), ?::jsonb)
            """;
            double weight = EVENT_WEIGHTS.getOrDefault(eventType, 0.1);
            jdbcTemplate.update(sql, clientId, productId, eventType, weight, metaJson);

        } catch (Exception e){
            log.error("Erreur événement IA avec meta: {}", e.getMessage());
        }
    }

}