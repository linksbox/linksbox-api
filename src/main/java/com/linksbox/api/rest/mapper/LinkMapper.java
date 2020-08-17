package com.linksbox.api.rest.mapper;

import com.linksbox.api.rest.model.LinkData;
import com.linksbox.api.rest.model.LinkInput;
import com.linksbox.model.Link;

public class LinkMapper {

	public LinkData mapToRestAPI(Link entity) {

		LinkData result = null;
		if (entity != null) {
			result = new LinkData();
			result.setUuid(entity.getUuid());
			result.setTitle(entity.getTitle());
			result.setUrl(entity.getUrl());
			result.setNotes(entity.getNotes());
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
