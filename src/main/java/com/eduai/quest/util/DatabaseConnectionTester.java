package com.eduai.quest.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseConnectionTester implements CommandLineRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        testDatabaseConnection();
    }

    private void testDatabaseConnection() {
        try {
            // Test connection
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                log.info("✅ Database connected successfully:");
                log.info("   - Database: {}", metaData.getDatabaseProductName());
                log.info("   - Version: {}", metaData.getDatabaseProductVersion());
                log.info("   - URL: {}", metaData.getURL());
            }

            // Test query execution
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            if (result != null && result == 1) {
                log.info("✅ Database query test successful");
            }

            // Check if tables exist
            String[] tables = {"users", "courses", "modules", "lessons", "quizzes"};
            for (String table : tables) {
                try {
                    jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
                    log.info("✅ Table '{}' exists", table);
                } catch (Exception e) {
                    log.warn("⚠️ Table '{}' does not exist or is empty", table);
                }
            }

        } catch (Exception e) {
            log.error("❌ Database connection failed: {}", e.getMessage());
            log.error("Please ensure:");
            log.error("1. MySQL is running on localhost:3306");
            log.error("2. Database 'eduai_quest' exists");
            log.error("3. User 'root' has access with password 'password'");
            log.error("4. Or run: docker-compose -f docker/docker-compose.yml up -d");
        }
    }
}