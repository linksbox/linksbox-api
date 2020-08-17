package com.linksbox.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.linksbox.model.Link;
import com.linksbox.model.Tag;

/**
 * @author habib
 *
 */

public interface LinkService {

	List<Link> getLinks();
	
	List<Link> getLinksByTags(List<Tag> tags);

	Optional<Link> getLinkByUuid(UUID uuid);

	Link createOrUpdateLink(Link link);
	
	void deleteLink(Link link);
}
