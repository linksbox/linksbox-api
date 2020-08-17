package com.linksbox.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksbox.model.Link;
import com.linksbox.model.Tag;
import com.linksbox.persistence.LinkRepository;
import com.linksbox.service.LinkService;

@Service
public class LinkServiceImpl implements LinkService {

	@Autowired
	LinkRepository repo;

	@Override
	public List<Link> getLinks() {
		return repo.findAll();
	}

	@Override
	public Optional<Link> getLinkByUuid(UUID uuid) {
		return repo.findByUuid(uuid);
	}

	@Override
	public Link createOrUpdateLink(Link link) {
		return repo.save(link);
	}

	@Override
	public void deleteLink(Link link) {
		repo.delete(link);
	}

	@Override
	public List<Link> getLinksByTags(List<Tag> tags) {
		return repo.findByTagsIn(tags);
	}

}
