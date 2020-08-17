package com.linksbox.api.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.linksbox.api.rest.mapper.LinkMapper;
import com.linksbox.api.rest.mapper.TagMapper;
import com.linksbox.api.rest.model.LinkData;
import com.linksbox.api.rest.model.LinkInput;
import com.linksbox.model.Link;
import com.linksbox.model.Tag;
import com.linksbox.service.LinkService;
import com.linksbox.service.TagService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class LinksApiDelegateImpl implements LinksApiDelegate {

	@Autowired
	private LinkService linkService;

	@Autowired
	private TagService tagService;

	LinkMapper mapper = new LinkMapper();
	TagMapper tagMapper = new TagMapper();

	@Override
	public ResponseEntity<List<LinkData>> getLinks(List<UUID> tagUuids) {

		Set<LinkData> result = new HashSet<>();

		if (!CollectionUtils.isEmpty(tagUuids)) {
			for (UUID uuid : tagUuids) {
				List<LinkData> links = tagService.getTagByUuid(uuid).get().getLinks().stream()
						.map(link -> mapper.mapToRestAPI(link)).collect(Collectors.toList());
				result.addAll(links);
			}
		} else {
			result = linkService.getLinks().stream().map(link -> mapper.mapToRestAPI(link)).collect(Collectors.toSet());
		}
		return ResponseEntity.ok().body(new ArrayList<>(result));
	}

	@Override
	public ResponseEntity<LinkData> getLinkByUuid(UUID uuid) {

		Optional<Link> link = linkService.getLinkByUuid(uuid);
		if (!link.isPresent()) {
			throw new IllegalArgumentException("Link Not found");
		}
		return ResponseEntity.ok().body(mapper.mapToRestAPI(link.get()));
	}

	@Override
	public ResponseEntity<LinkData> createLink(List<UUID> tagUuids, LinkInput linkInput) {

		if (CollectionUtils.isEmpty(tagUuids)) {
			throw new IllegalArgumentException("Tags are mondatory for the link creation!");
		}
		log.info("> createLink...");

		LinkData response = new LinkData();
		try {
			Link link = mapper.mapToEntity(linkInput);
			link.setTags(getTags(tagUuids));
			linkService.createOrUpdateLink(link);
			response = mapper.mapToRestAPI(link);
		} catch (Exception e) {
			log.error("Exception in link creation: {}", e);
		}
		return ResponseEntity.ok().body(response);
	}

	@Override
	public ResponseEntity<LinkData> updateLink(UUID uuid, LinkInput linkInput) {

		Optional<Link> link = linkService.getLinkByUuid(uuid);
		if (!link.isPresent()) {
			throw new IllegalArgumentException("Link Not found");
		}
		LinkData response = new LinkData();
		try {
			Link linkUp = mapper.patch(linkInput, link.get());
			linkService.createOrUpdateLink(linkUp);
			response = mapper.mapToRestAPI(linkUp);
		} catch (Exception e) {
			log.error("Exception in link updating: {}", e);
		}
		return ResponseEntity.ok().body(response);
	}

	@Override
	public ResponseEntity<Void> deleteLink(UUID uuid) {

		Optional<Link> link = linkService.getLinkByUuid(uuid);
		if (!link.isPresent()) {
			throw new IllegalArgumentException("Link Not found");
		}
		try {
			linkService.deleteLink(link.get());
		} catch (Exception e) {
			log.error("Exception in link deleting: {}", e);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// **************** Private methods ******************

	private List<Tag> getTags(List<UUID> tagUuids) {
		return tagUuids.stream().map(uuid -> tagService.getTagByUuid(uuid).get()).collect(Collectors.toList());
	}
}
