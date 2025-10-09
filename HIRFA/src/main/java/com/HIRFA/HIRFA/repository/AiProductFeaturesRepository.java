package com.HIRFA.HIRFA.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AiProductFeaturesRepository {
    private final NamedParameterJdbcTemplate aiNamedJdbc;

    /** Upsert embedding (vector(384) en texte: '[0.1, 0.2, ...]') */
    public void upsertEmbedding(String productId, String embeddingArrayLiteral, OffsetDateTime updatedAt) {
        String sql = """
      INSERT INTO ai.product_features (product_id, text_embedding, updated_at)
      VALUES (:pid, :emb::vector, :updated)
      ON CONFLICT (product_id) DO UPDATE
        SET text_embedding = EXCLUDED.text_embedding,
            updated_at = EXCLUDED.updated_at
      """;
        aiNamedJdbc.update(sql, new MapSqlParameterSource()
                .addValue("pid", productId)
                .addValue("emb", embeddingArrayLiteral)
                .addValue("updated", updatedAt));
    }

    public void updatePopularity(String productId, double pop7, double pop30) {
        String sql = """
      UPDATE ai.product_features
      SET popularity_7d=:p7, popularity_30d=:p30, updated_at=now()
      WHERE product_id=:pid
      """;
        aiNamedJdbc.update(sql, new MapSqlParameterSource()
                .addValue("pid", productId).addValue("p7", pop7).addValue("p30", pop30));
    }

    public void syncCaches(String productId, Double priceMad, Boolean inStock,
                           String category, String flavor, List<String> tags) {
        String sql = """
      INSERT INTO ai.product_features (product_id, price_mad, in_stock, category, flavor, tags, updated_at)
      VALUES (:pid, :price, :stock, :cat, :flv, :tags, now())
      ON CONFLICT (product_id) DO UPDATE
        SET price_mad = EXCLUDED.price_mad,
            in_stock  = EXCLUDED.in_stock,
            category  = EXCLUDED.category,
            flavor    = EXCLUDED.flavor,
            tags      = EXCLUDED.tags,
            updated_at= now()
      """;
        aiNamedJdbc.update(sql, new MapSqlParameterSource()
                .addValue("pid", productId)
                .addValue("price", priceMad)
                .addValue("stock", inStock)
                .addValue("cat", category)
                .addValue("flv", flavor)
                .addValue("tags", tags == null ? new String[]{} : tags.toArray(String[]::new)));
    }
}
