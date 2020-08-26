package com.linksbox.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.linksbox.model.Link;
import com.linksbox.model.Tag;

/**
 * @author habib
 *
 */

@Repository
public interface LinkRepository extends JpaRepository<Link, Long>, JpaSpecificationExecutor<Link> {

	Optional<Link> findByUuid(UUID uuid);

	List<Link> findByTagsIn(List<Tag> tags);
	
	Optional<Link> findByUrl(String url);
}
