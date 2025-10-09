package com.HIRFA.HIRFA.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductPopularityJob {

    private final JdbcTemplate aiJdbc; // @Qualifier("aiJdbcTemplate")

    /** calcule une popularité simple pondérée (7j & 30j) chaque nuit */
    @Scheduled(cron = "0 15 2 * * *") // 02:15 tous les jours
    public void recomputePopularity() {
        // 7 jours
        String sql7 = """
      WITH s AS (
        SELECT product_id, SUM(weight) AS score
        FROM ai.user_events
        WHERE ts >= now() - INTERVAL '7 days'
        GROUP BY product_id
      )
      UPDATE ai.product_features pf
      SET popularity_7d = COALESCE(s.score,0), updated_at = now()
      FROM s WHERE pf.product_id = s.product_id
      """;
        aiJdbc.update(sql7);

        // 30 jours
        String sql30 = """
      WITH s AS (
        SELECT product_id, SUM(weight) AS score
        FROM ai.user_events
        WHERE ts >= now() - INTERVAL '30 days'
        GROUP BY product_id
      )
      UPDATE ai.product_features pf
      SET popularity_30d = COALESCE(s.score,0), updated_at = now()
      FROM s WHERE pf.product_id = s.product_id
      """;
        aiJdbc.update(sql30);
    }
}
