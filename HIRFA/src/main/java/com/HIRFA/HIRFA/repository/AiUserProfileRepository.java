package com.HIRFA.HIRFA.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiUserProfileRepository {
    private final NamedParameterJdbcTemplate aiNamedJdbc;

    public void upsertBudget(String userId, Double min, Double max) {
        String sql = """
      INSERT INTO ai.user_profile (user_id, budget_min, budget_max, updated_at)
      VALUES (:uid, :min, :max, now())
      ON CONFLICT (user_id) DO UPDATE
        SET budget_min = EXCLUDED.budget_min,
            budget_max = EXCLUDED.budget_max,
            updated_at = now()
      """;
        aiNamedJdbc.update(sql, new MapSqlParameterSource()
                .addValue("uid", userId).addValue("min", min).addValue("max", max));
    }
}
