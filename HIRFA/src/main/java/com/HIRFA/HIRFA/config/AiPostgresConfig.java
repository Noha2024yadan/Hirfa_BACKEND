//package com.HIRFA.HIRFA.config;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.*;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class AiPostgresConfig {
//
//    @Bean
//    @ConfigurationProperties("spring.ai-datasource")
//    public DataSourceProperties aiDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean(name = "aiDataSource")
//    public DataSource aiDataSource(@Qualifier("aiDataSourceProperties") DataSourceProperties props) {
//        HikariConfig cfg = new HikariConfig();
//        cfg.setJdbcUrl(props.getUrl());
//        cfg.setUsername(props.getUsername());
//        cfg.setPassword(props.getPassword());
//        cfg.setDriverClassName(props.getDriverClassName());
//        return new HikariDataSource(cfg);
//    }
//
//    @Bean(name = "aiJdbcTemplate")
//    public JdbcTemplate aiJdbcTemplate(@Qualifier("aiDataSource") DataSource ds) {
//        return new JdbcTemplate(ds);
//    }
//
//    @Bean(name = "aiNamedJdbc")
//    public NamedParameterJdbcTemplate aiNamedJdbc(@Qualifier("aiDataSource") DataSource ds) {
//        return new NamedParameterJdbcTemplate(ds);
//    }
//
//    @Bean(name = "aiTxManager")
//    public PlatformTransactionManager aiTxManager(@Qualifier("aiDataSource") DataSource ds) {
//        return new DataSourceTransactionManager(ds);
//    }
//
//    /** Flyway dédié aux tables IA */
//    @Bean(initMethod = "migrate")
//    public Flyway aiFlyway(@Qualifier("aiDataSource") DataSource ds) {
//        return Flyway.configure()
//                .schemas("ai")
//                .locations("classpath:db/ai/migration")
//                .dataSource(ds)
//                .baselineOnMigrate(true)
//                .load();
//    }
//}
