package com.mottu.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Arrays;

@Component
public class DataSourceInspector {

    private static final Logger log = LoggerFactory.getLogger(DataSourceInspector.class);

    @Autowired
    private Environment env;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void inspect() {
        try {
            String[] profiles = env.getActiveProfiles();
            if (profiles == null || profiles.length == 0) {
                profiles = env.getDefaultProfiles();
            }
            log.info("Active Spring profiles: {}", Arrays.toString(profiles));

            String cfgUrl = env.getProperty("spring.datasource.url");
            String cfgUser = env.getProperty("spring.datasource.username");
            log.info("Configured datasource.url='{}' datasource.username='{}'", cfgUrl, cfgUser != null ? "****" : null);

            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData md = conn.getMetaData();
                log.info("JDBC URL via DataSource: {}", md.getURL());
                log.info("DB Product: {} {}", md.getDatabaseProductName(), md.getDatabaseProductVersion());
                log.info("DB User (connection): {}", md.getUserName());

                // Try a quick count on MOTO table to know if it's empty or has rows
                try {
                    Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MOTO", Integer.class);
                    log.info("Count from MOTO table: {}", count);
                } catch (Exception e) {
                    log.warn("Could not query MOTO table (may not exist on this DB/schema): {}", e.getMessage());
                }

            }
        } catch (Exception e) {
            log.error("Error while inspecting DataSource: {}", e.getMessage(), e);
        }
    }
}

