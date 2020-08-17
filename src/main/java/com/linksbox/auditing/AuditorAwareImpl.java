package com.linksbox.auditing;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.linksbox.model.AbstractBaseEntity;

/**
 * Custom AuditorAware implementation for Spring-Auditing of '@lastModifidBy' and '@lastCreatedBy'
 * {@link AbstractBaseEntity}. Auditing logic for Modifier and Creator are implemented here
 *
 */

public class AuditorAwareImpl implements AuditorAware<String> {

	private static final String NOMEN_NESCIO = "N.N.";

	@Override
	public Optional<String> getCurrentAuditor() {
		//String actorName = ActorContextHolder.getActor().getName();
		String actorName = System.getProperty("user.name");
		return actorName != null ? Optional.of(actorName) : Optional.of(NOMEN_NESCIO);
	}
}
