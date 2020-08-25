package com.linksbox.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linksbox.model.Tag;

/**
 * @author habib
 *
 */

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
	
	Optional<Tag> findByUuid(UUID uuid);
	
	Optional<Tag> findByName(String name);
}
