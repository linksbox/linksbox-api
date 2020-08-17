package com.linksbox.api.rest.mapper;

import com.linksbox.api.rest.model.TagData;
import com.linksbox.api.rest.model.TagInput;
import com.linksbox.model.Tag;

public class TagMapper {

	public TagData mapToRestAPI(Tag entity) {

		TagData result = null;
		if (entity != null) {
			result = new TagData();
			result.setUuid(entity.getUuid());
			result.setName(entity.getName());
		}
		return result;
	}

	public Tag mapToEntity(TagInput input) {

		Tag result = null;
		if (input != null) {
			result = new Tag();
			result.setName(input.getName());
		}
		return result;
	}

	public Tag patch(TagInput input, Tag to) {

		Tag result = to;
		if (input != null) {
			result.setName(input.getName());
		}
		return result;
	}
}
