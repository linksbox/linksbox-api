package com.linksbox.api.rest.mapper;

import java.util.stream.Collectors;

import com.linksbox.api.rest.model.LinkData;
import com.linksbox.api.rest.model.LinkInput;
import com.linksbox.model.Link;

public class LinkMapper {

	TagMapper tagMapper = new TagMapper();

	public static LinkData mapToRestAPI(Link entity) {

		LinkData result = null;
		if (entity != null) {
			result = new LinkData();
			result.setUuid(entity.getUuid());
			result.setTitle(entity.getTitle());
			result.setUrl(entity.getUrl());
			result.setNotes(entity.getNotes());
			result.setViews(entity.getViews());
			result.setCreationDate(entity.getCreated());
			result.setTags(
					entity.getTags().stream().map(Tag -> TagMapper.mapToRestAPI(Tag)).collect(Collectors.toList()));
		}
		return result;
	}

	public static Link mapToEntity(LinkInput input) {

		Link result = null;
		if (input != null) {
			result = new Link();
			result.setTitle(input.getTitle());
			result.setUrl(input.getUrl());
			result.setNotes(input.getNotes());
			result.setViews(input.getViews());
		}
		return result;
	}

	public static void patch(LinkData linkData, Link to) {

		if (linkData != null) {
			to.setTitle(linkData.getTitle());
			to.setUrl(linkData.getUrl());
			to.setNotes(linkData.getNotes());
		}
	}
}
