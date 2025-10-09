package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.repository.AiProductFeaturesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductFeaturesSyncJob {

    private final JdbcTemplate jdbc; // MySQL (primary, auto-config)
    private final AiProductFeaturesRepository aiRepo;

    /** met à jour les caches prix/stock/catégorie/tags depuis ta table produits MySQL */
    @Scheduled(cron = "0 40 2 * * *") // 02:40
    public void sync() {

        jdbc.query("""
        SELECT id, price_mad, in_stock, category, flavor, tags
        FROM products
        """, rs -> {
            String pid = rs.getString("id");
            Double price = rs.getDouble("price_mad");
            Boolean stock = rs.getBoolean("in_stock");
            String cat = rs.getString("category");
            String flv = rs.getString("flavor");
            String tagsCsv = rs.getString("tags"); // ex: "bio,local"
            List<String> tags = tagsCsv == null ? List.of() : List.of(tagsCsv.split("\\s*,\\s*"));
            aiRepo.syncCaches(pid, price, stock, cat, flv, tags);
        });
    }
}
