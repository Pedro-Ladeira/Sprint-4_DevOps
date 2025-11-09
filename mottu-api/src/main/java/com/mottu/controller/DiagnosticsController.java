package com.mottu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/diagnostics")
public class DiagnosticsController {

    @Autowired
    private Environment env;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public Map<String, Object> diagnostics() {
        Map<String, Object> out = new HashMap<>();
        String[] profiles = env.getActiveProfiles();
        out.put("activeProfiles", profiles == null || profiles.length == 0 ? env.getDefaultProfiles() : profiles);
        out.put("configured.datasource.url", env.getProperty("spring.datasource.url"));
        out.put("configured.datasource.username", env.getProperty("spring.datasource.username") != null ? "****" : null);

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData md = conn.getMetaData();
            out.put("jdbc.url", md.getURL());
            out.put("jdbc.user", md.getUserName());
            out.put("db.product", md.getDatabaseProductName());
            out.put("db.version", md.getDatabaseProductVersion());
        } catch (Exception e) {
            out.put("connection.error", e.getMessage());
        }

        // try counts
        try {
            Integer motoCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MOTO", Integer.class);
            out.put("count.MOTO", motoCount);
        } catch (Exception e) {
            out.put("count.MOTO.error", e.getMessage());
        }
        try {
            Integer modeloCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MODELO", Integer.class);
            out.put("count.MODELO", modeloCount);
        } catch (Exception e) {
            out.put("count.MODELO.error", e.getMessage());
        }
        try {
            Integer patioCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM PATIO", Integer.class);
            out.put("count.PATIO", patioCount);
        } catch (Exception e) {
            out.put("count.PATIO.error", e.getMessage());
        }

        return out;
    }
}

