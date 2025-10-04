package com.eduai.quest.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseHealthCheck implements HealthIndicator {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Health health() {
        try {
            // Test database connection
            try (Connection connection = dataSource.getConnection()) {
                if (connection.isValid(1000)) {
                    // Test basic query
                    Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
                    if (result != null && result == 1) {
                        return Health.up()
                                .withDetail("database", "MySQL")
                                .withDetail("validationQuery", "SELECT 1")
                                .withDetail("status", "connected")
                                .build();
                    }
                }
            }

            return Health.down()
                    .withDetail("database", "MySQL")
                    .withDetail("error", "Connection validation failed")
                    .build();

        } catch (Exception e) {
            log.error("Database health check failed", e);
            return Health.down(e)
                    .withDetail("database", "MySQL")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}