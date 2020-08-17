package com.linksbox.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.linksbox.model.Tag;

/**
 * @author habib
 *
 */

public interface TagService {

	List<Tag> getTags();

	Optional<Tag> getTagByUuid(UUID uuid);

	Tag createOrUpdateTag(Tag tag);

	void deleteTag(Tag tag);
}
