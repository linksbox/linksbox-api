package com.linksbox.api.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.linksbox.api.rest.mapper.LinkMapper;
import com.linksbox.api.rest.mapper.TagMapper;
import com.linksbox.api.rest.model.LinkData;
import com.linksbox.api.rest.model.LinkInput;
import com.linksbox.api.rest.model.LinksData;
import com.linksbox.exception.ErrorKey;
import com.linksbox.exception.RestApiServerException;
import com.linksbox.exception.RestApiValidationException;
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
	public ResponseEntity<LinksData> search(String searchText, Boolean newSearch, Integer page, Integer size) {

		if (searchText == null || searchText.trim().isEmpty()) {
			throw new RestApiValidationException(ErrorKey.SEARCH_TEXT, "Enter a free text to search link...");
		}

		LinksData result = new LinksData();

		Page<Link> linkPage = linkService.search(searchText, PageRequest.of(page, size, Sort.by("views").descending()));
		List<LinkData> links = linkPage.getContent().stream().map(link -> mapper.mapToRestAPI(link))
				.collect(Collectors.toList());
		result.setLinks(links);
		result.setCurrentPage(linkPage.getNumber());
		result.setTotalElements((int) linkPage.getTotalElements());
		result.setTotalPages(linkPage.getTotalPages());

		return ResponseEntity.ok().body(result);
	}

	@Override
	public ResponseEntity<LinksData> getLinks(List<UUID> tagUuids, Integer page, Integer size) {

		LinksData result = new LinksData();
		List<LinkData> links = null;

		if (CollectionUtils.isNotEmpty(tagUuids)) {
			for (UUID uuid : tagUuids) {
				links = tagService.getTagByUuid(uuid).get().getLinks().stream().map(link -> mapper.mapToRestAPI(link))
						.collect(Collectors.toList());
				result.setLinks(links);
			}
		} else {
			Page<Link> linkPage = linkService.getLinks(PageRequest.of(page, size, Sort.by("views").descending()));
			links = linkPage.getContent().stream().map(link -> mapper.mapToRestAPI(link)).collect(Collectors.toList());
			result.setLinks(links);
			result.setCurrentPage(linkPage.getNumber());
			result.setTotalElements((int) linkPage.getTotalElements());
			result.setTotalPages(linkPage.getTotalPages());
		}
		return ResponseEntity.ok().body(result);
	}

	@Override
	public ResponseEntity<LinkData> incrementLinkViews(UUID uuid) {

		Optional<Link> link = linkService.getLinkByUuid(uuid);
		if (!link.isPresent()) {
			throw new RestApiValidationException(ErrorKey.LINK_URL, "Link Not found");
		}
		Link gotLink = link.get();
		try {
			gotLink.setViews(gotLink.getViews() + 1);
			linkService.createOrUpdateLink(gotLink);
		} catch (Exception e) {
			log.error("Exception in incrementing link views: {}", e);
			throw new RestApiServerException(e.getMessage());
		}
		return ResponseEntity.ok().body(mapper.mapToRestAPI(gotLink));
	}

	@Override
	public ResponseEntity<LinkData> getLinkByUuid(UUID uuid) {

		Optional<Link> link = linkService.getLinkByUuid(uuid);
		if (!link.isPresent()) {
			throw new RestApiValidationException(ErrorKey.LINK_URL, "Link Not found");
		}
		return ResponseEntity.ok().body(mapper.mapToRestAPI(link.get()));
	}

	@Override
	public ResponseEntity<LinkData> createLink(List<UUID> tagUuids, LinkInput linkInput) {

		if (linkService.getLinkByUrl(linkInput.getUrl()).isPresent()) {
			throw new RestApiValidationException(ErrorKey.LINK_URL, "Link URL already exist!");
		}

		log.info("> createLink...");

		LinkData response = new LinkData();
		try {
			Link link = mapper.mapToEntity(linkInput);
			if (CollectionUtils.isNotEmpty(tagUuids)) {
				link.setTags(getTags(tagUuids));
			}
			linkService.createOrUpdateLink(link);
			response = mapper.mapToRestAPI(link);
		} catch (Exception e) {
			log.error("Exception in link creation: {}", e);
			throw new RestApiServerException(e.getMessage());
		}
		return ResponseEntity.ok().body(response);
	}

	@Override
	public ResponseEntity<LinkData> updateLink(UUID uuid, LinkInput linkInput) {

		Optional<Link> link = linkService.getLinkByUuid(uuid);
		if (!link.isPresent()) {
			throw new RestApiValidationException(ErrorKey.LINK_URL, "Link Not found");
		}
		LinkData response = new LinkData();
		try {
			Link linkUp = mapper.patch(linkInput, link.get());
			linkService.createOrUpdateLink(linkUp);
			response = mapper.mapToRestAPI(linkUp);
		} catch (Exception e) {
			log.error("Exception in link updating: {}", e);
			throw new RestApiServerException(e.getMessage());
		}
		return ResponseEntity.ok().body(response);
	}

	@Override
	public ResponseEntity<Void> deleteLink(UUID uuid) {

		Optional<Link> link = linkService.getLinkByUuid(uuid);
		if (!link.isPresent()) {
			throw new RestApiValidationException(ErrorKey.LINK_URL, "Link Not found");
		}
		try {
			linkService.deleteLink(link.get());
		} catch (Exception e) {
			log.error("Exception in link deleting: {}", e);
			throw new RestApiServerException(e.getMessage());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// **************** Private methods ******************

	private List<Tag> getTags(List<UUID> tagUuids) {
		return tagUuids.stream().map(uuid -> tagService.getTagByUuid(uuid).get()).collect(Collectors.toList());
	}

}
