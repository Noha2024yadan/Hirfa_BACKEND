package com.HIRFA.HIRFA.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AIProfileService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void updateUserProfile(UUID clientId, Double budgetMin, Double budgetMax,
                                  Map<String, Object> prefs, String cityId, String language) {
        try {
            String prefsJson = objectMapper.writeValueAsString(prefs);

            String sql = """
                INSERT INTO ai.user_profile 
                (client_id, budget_min, budget_max, prefs, city_id, language)
                VALUES (?, ?, ?, ?::jsonb, ?, ?)
                ON CONFLICT (client_id) DO UPDATE SET
                    budget_min = EXCLUDED.budget_min,
                    budget_max = EXCLUDED.budget_max,
                    prefs = EXCLUDED.prefs,
                    city_id = EXCLUDED.city_id,
                    language = EXCLUDED.language
            """;

            jdbcTemplate.update(sql, clientId, budgetMin, budgetMax, prefsJson, cityId, language);

            log.info("✅ Profil IA mis à jour pour client: {}", clientId);

        } catch (Exception e) {
            log.error("❌ Erreur mise à jour profil IA: {}", e.getMessage());
        }
    }
}