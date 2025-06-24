package com.example.cluvrapi.global.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnProperty(name = "audit.enabled", havingValue = "true", matchIfMissing = true)
@EnableJpaAuditing
public class JpaAuditingConfig {
}