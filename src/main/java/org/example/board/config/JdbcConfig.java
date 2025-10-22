package org.example.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

import java.util.Optional;

/**
 * Spring Data JDBC Auditing 기능 설정 클래스
 *
 * @author jeongdahee
 * @since 2025-10-21
 */
@Configuration
@EnableJdbcAuditing
public class JdbcConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("SYSTEM");
    }
}
