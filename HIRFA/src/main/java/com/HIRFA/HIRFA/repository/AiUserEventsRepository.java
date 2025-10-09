package com.HIRFA.HIRFA.repository;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Map;

@Repository
public class AiUserEventsRepository {

    private final NamedParameterJdbcTemplate aiNamedJdbc;

    public AiUserEventsRepository(NamedParameterJdbcTemplate aiNamedJdbc) {
        this.aiNamedJdbc = aiNamedJdbc;
    }

    public void insertEvent(String userId, String productId, String eventType, double weight,
                            OffsetDateTime ts, Map<String, Object> meta) {
        String sql = """
            INSERT INTO ai.user_events (user_id, product_id, event_type, weight, ts, meta)
            VALUES (:userId, :productId, :eventType, :weight, :ts, to_jsonb(:meta::json))
            """;

        String metaJson;
        try {
            metaJson = (meta == null)
                    ? "{}"
                    : JsonMapper.builder().build().writeValueAsString(meta);
        } catch (Exception e) {
            metaJson = "{}";
        }

        var params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("productId", productId)
                .addValue("eventType", eventType)
                .addValue("weight", weight)
                .addValue("ts", ts)
                .addValue("meta", metaJson);

        aiNamedJdbc.update(sql, params);
    }
}
