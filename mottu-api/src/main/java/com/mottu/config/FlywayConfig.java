//package com.mottu.config;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//import java.nio.charset.StandardCharsets;
//
//@Configuration
//public class FlywayConfig {
//
//    @Bean
//    public Flyway flyway(DataSource dataSource) {
//        Flyway flyway = Flyway.configure()
//                .dataSource(dataSource)  // <- ESSENCIAL
//                .locations("classpath:db/migration")
//                .baselineOnMigrate(true)
//                .baselineVersion("0")
//                .validateOnMigrate(false)
//                .encoding(StandardCharsets.UTF_8)
//                .load();
//        flyway.migrate();
//        return flyway;
//    }
//
//}
