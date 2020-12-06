package com.linksbox.api.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.linksbox.api.rest.mapper.TagMapper;
import com.linksbox.api.rest.model.TagData;
import com.linksbox.api.rest.model.TagInput;
import com.linksbox.exception.ErrorKey;
import com.linksbox.exception.RestApiServerException;
import com.linksbox.exception.RestApiValidationException;
import com.linksbox.model.Tag;
import com.linksbox.service.TagService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TagsApiDelegateImpl implements TagsApiDelegate {

	@Autowired
	private TagService tagService;

	@Override
	public ResponseEntity<List<TagData>> getTags() {
		List<TagData> tags = tagService.getTags().stream().map(Tag -> TagMapper.mapToRestAPI(Tag))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(tags);
	}

	@Override
	public ResponseEntity<TagData> getTagByUuid(UUID uuid) {
		Optional<Tag> tag = tagService.getTagByUuid(uuid);
		if (!tag.isPresent()) {
			throw new RestApiValidationException("Tag Not found");
		}
		return ResponseEntity.ok().body(TagMapper.mapToRestAPI(tag.get()));
	}

	@Override
	public ResponseEntity<TagData> createTag(TagInput tagInput) {

		if (tagService.getTagByName(tagInput.getName()).isPresent()) {
			throw new RestApiValidationException(ErrorKey.TAG_NAME, "Tag already exist!");
		}

		log.info("> createTag...");
		TagData response = new TagData();
		try {
			Tag tag = TagMapper.mapToEntity(tagInput);
			tagService.createOrUpdateTag(tag);
			response = TagMapper.mapToRestAPI(tag);
		} catch (Exception e) {
			log.error("Exception in Tag creation: {}", e);
			throw new RestApiServerException(e.getMessage());
		}
		return ResponseEntity.ok().body(response);
	}

	@Override
	public ResponseEntity<TagData> updateTag(UUID uuid, TagInput tagInput) {
		Optional<Tag> tag = tagService.getTagByUuid(uuid);
		if (!tag.isPresent()) {
			throw new RestApiValidationException("Tag Not found");
		}
		TagData response = new TagData();
		try {
			Tag gotTag = tag.get();
			TagMapper.patch(tagInput, gotTag);
			tagService.createOrUpdateTag(gotTag);
			response = TagMapper.mapToRestAPI(gotTag);
		} catch (Exception e) {
			log.error("Exception in Tag creation: {}", e);
			throw new RestApiServerException(e.getMessage());
		}
		return ResponseEntity.ok().body(response);
	}

	@Override
	public ResponseEntity<Void> deleteTag(UUID uuid) {
		Optional<Tag> tag = tagService.getTagByUuid(uuid);
		if (!tag.isPresent()) {
			throw new RestApiValidationException("Tag Not found");
		}
		try {
			tagService.deleteTag(tag.get());
		} catch (Exception e) {
			log.error("Exception in Tag creation: {}", e);
			throw new RestApiServerException(e.getMessage());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
