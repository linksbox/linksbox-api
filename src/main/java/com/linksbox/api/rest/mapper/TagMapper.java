package com.linksbox.api.rest.mapper;

import com.linksbox.api.rest.model.TagData;
import com.linksbox.api.rest.model.TagInput;
import com.linksbox.model.Tag;

public class TagMapper {

	public static TagData mapToRestAPI(Tag entity) {

		TagData result = null;
		if (entity != null) {
			result = new TagData();
			result.setUuid(entity.getUuid());
			result.setName(entity.getName());
		}
		return result;
	}

	public static Tag mapToEntity(TagInput input) {

		Tag result = null;
		if (input != null) {
			result = new Tag();
			result.setName(input.getName());
		}
		return result;
	}

	public static void patch(TagInput input, Tag to) {

		if (input != null) {
			to.setName(input.getName());
		}
	}
}
