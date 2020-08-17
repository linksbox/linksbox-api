package com.linksbox.auditing;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.linksbox.model.AbstractBaseEntity;

/**
 * Configuration for Spring JPA-Auditing used in {@link AbstractBaseEntity}
 *
 */
@Configuration
@ConditionalOnClass(JpaRepository.class)
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
public class AuditConfiguration {

	@Bean
	AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl();
	}

	@Bean
	DateTimeProvider dateTimeProvider() {
		return new DateTimeProviderImpl();
	}

}