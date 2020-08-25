package com.linksbox.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksbox.model.Tag;
import com.linksbox.persistence.TagRepository;
import com.linksbox.service.TagService;

@Service
public class TagServiceImpl implements TagService {

	@Autowired
	TagRepository repo;

	@Override
	public List<Tag> getTags() {
		return repo.findAll();
	}

	@Override
	public Optional<Tag> getTagByUuid(UUID uuid) {
		return repo.findByUuid(uuid);
	}

	@Override
	public Tag createOrUpdateTag(Tag tag) {
		return repo.save(tag);
	}

	@Override
	public void deleteTag(Tag tag) {
		repo.delete(tag);
	}

	@Override
	public Optional<Tag> getTagByName(String name) {
		return repo.findByName(name);
	}

}
