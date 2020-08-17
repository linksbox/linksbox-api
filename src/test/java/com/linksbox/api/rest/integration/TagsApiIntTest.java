package com.linksbox.api.rest.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.linksbox.AbstractTestBase;
import com.linksbox.LinksboxApplication;
import com.linksbox.model.Tag;

@SpringBootTest(classes = LinksboxApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class TagsApiIntTest extends AbstractTestBase{

	private final static String PREFIX_TAGS_API_URL = "/api/v1/tags";

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void getTags_returnTagsDetails() {
		restTemplate.postForEntity(PREFIX_TAGS_API_URL, createDummyTag(), Tag.class);

		ResponseEntity<Tag[]> response = restTemplate.getForEntity(PREFIX_TAGS_API_URL, Tag[].class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().length).isGreaterThan(0);
	}

	@Test
	void getTagByUuid_returnTagDetails() {
		Tag createdTag = restTemplate.postForEntity(PREFIX_TAGS_API_URL, createDummyTag(), Tag.class).getBody();

		ResponseEntity<Tag> response = restTemplate.getForEntity(PREFIX_TAGS_API_URL + "/" + createdTag.getUuid(),
				Tag.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().getName()).isEqualTo(createdTag.getName());
		Assertions.assertThat(response.getBody().getUuid()).isEqualTo(createdTag.getUuid());
	}

	@Test
	public void createTag_returnOk() {
		Tag tag = createDummyTag();
		ResponseEntity<Tag> response = restTemplate.postForEntity(PREFIX_TAGS_API_URL, tag, Tag.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().getName()).isEqualTo(tag.getName());
		Assertions.assertThat(response.getBody().getUuid()).isNotNull();
	}

	@Test
	void updateTag_returnOk() {
		String newTagName = "New Tag name";
		Tag createdTag = restTemplate.postForEntity(PREFIX_TAGS_API_URL, createDummyTag(), Tag.class).getBody();
		createdTag.setName(newTagName);
		HttpEntity<Tag> requestUpdate = new HttpEntity<>(createdTag);

		ResponseEntity<Tag> response = restTemplate.exchange(PREFIX_TAGS_API_URL + "/" + createdTag.getUuid(),
				HttpMethod.PUT, requestUpdate, Tag.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().getName()).isEqualTo(newTagName);
	}

	@Test
	void deleteTag_returnOk() {
		Tag createdTag = restTemplate.postForEntity(PREFIX_TAGS_API_URL, createDummyTag(), Tag.class).getBody();
		restTemplate.delete(PREFIX_TAGS_API_URL + "/" + createdTag.getUuid());
		
		ResponseEntity<Tag> response = restTemplate.getForEntity(PREFIX_TAGS_API_URL + "/" + createdTag.getUuid(),
				Tag.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Tag createDummyTag() {
		Tag tag = new Tag();
		tag.setName(generateString(10));
		return tag;
	}

}
