package com.linksbox.api.rest.mapper;

import java.util.stream.Collectors;

import com.linksbox.api.rest.model.LinkData;
import com.linksbox.api.rest.model.LinkInput;
import com.linksbox.model.Link;

public class LinkMapper {

	TagMapper tagMapper = new TagMapper();

	public LinkData mapToRestAPI(Link entity) {

		LinkData result = null;
		if (entity != null) {
			result = new LinkData();
			result.setUuid(entity.getUuid());
			result.setTitle(entity.getTitle());
			result.setUrl(entity.getUrl());
			result.setNotes(entity.getNotes());
			result.setTags(
					entity.getTags().stream().map(Tag -> tagMapper.mapToRestAPI(Tag)).collect(Collectors.toList()));
		}
		return result;
	}

	public Link mapToEntity(LinkInput input) {

		Link result = null;
		if (input != null) {
			result = new Link();
			result.setTitle(input.getTitle());
			result.setUrl(input.getUrl());
			result.setNotes(input.getNotes());
		}
		return result;
	}

	public Link patch(LinkInput input, Link to) {

		Link result = to;
		if (input != null) {
			result.setTitle(input.getTitle());
			result.setUrl(input.getUrl());
			result.setNotes(input.getNotes());
		}
		return result;
	}
}
