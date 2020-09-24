package com.linksbox.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.linksbox.model.Link;
import com.linksbox.model.Tag;

/**
 * @author habib
 *
 */

public interface LinkService {

	Page<Link> getLinks(Pageable pageable);
	
	Page<Link> search(String searchText, Pageable pageable);
	
	List<Link> getLinksByTags(List<Tag> tags);

	Optional<Link> getLinkByUuid(UUID uuid);
	
	Optional<Link> getLinkByUrl(String url);

	Link createOrUpdateLink(Link link);
	
	// test for Imed
	
	void deleteLink(Link link);
}
