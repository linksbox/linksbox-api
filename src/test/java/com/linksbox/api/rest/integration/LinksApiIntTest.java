package com.linksbox.api.rest.integration;

import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.linksbox.AbstractTestBase;
import com.linksbox.LinksboxApplication;
import com.linksbox.api.rest.model.LinkData;
import com.linksbox.api.rest.model.LinkInput;
import com.linksbox.model.Tag;

@SpringBootTest(classes = LinksboxApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class LinksApiIntTest extends AbstractTestBase {

	private final static String PREFIX_LINKS_API_URL = "/api/v1/links";
	private final static String PREFIX_TAGS_API_URL = "/api/v1/tags";

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void getLinks_returnLinksDetails() {
		postLink();
		ResponseEntity<LinkData[]> response = restTemplate.getForEntity(PREFIX_LINKS_API_URL, LinkData[].class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().length).isGreaterThan(0);
	}

	@Test
	void getLinkByUuid_returnLinkDetails() {
		LinkData createdLink = postLink();
		ResponseEntity<LinkData> response = restTemplate
				.getForEntity(PREFIX_LINKS_API_URL + "/" + createdLink.getUuid(), LinkData.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().getTitle()).isEqualTo(createdLink.getTitle());
		Assertions.assertThat(response.getBody().getUuid()).isEqualTo(createdLink.getUuid());
	}

	@Test
	public void createLink_returnOk() {

		LinkInput link = createDummyLink();
		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(PREFIX_LINKS_API_URL)
				// Add query parameter
				.queryParam("tagUuids", Arrays.asList(createTag()));

		HttpEntity<LinkInput> requestCreate = new HttpEntity<>(link);
		ResponseEntity<LinkData> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, requestCreate,
				LinkData.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().getTitle()).isEqualTo(link.getTitle());
		Assertions.assertThat(response.getBody().getUrl()).isEqualTo(link.getUrl());
		Assertions.assertThat(response.getBody().getUuid()).isNotNull();
	}

	@Test
	void updateLink_returnOk() {
		String newTagTitle = "New link Title";
		LinkData createdLink = postLink();
		createdLink.setTitle(newTagTitle);
		HttpEntity<LinkData> requestUpdate = new HttpEntity<>(createdLink);

		ResponseEntity<LinkData> response = restTemplate.exchange(PREFIX_LINKS_API_URL + "/" + createdLink.getUuid(),
				HttpMethod.PUT, requestUpdate, LinkData.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().getTitle()).isEqualTo(newTagTitle);
	}

	@Test
	void deleteLink_returnOk() {
		LinkData createdLink = postLink();
		restTemplate.delete(PREFIX_LINKS_API_URL + "/" + createdLink.getUuid());

		ResponseEntity<LinkData> response = restTemplate
				.getForEntity(PREFIX_LINKS_API_URL + "/" + createdLink.getUuid(), LinkData.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private LinkData postLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(PREFIX_LINKS_API_URL).queryParam("tagUuids",
				Arrays.asList(createTag()));
		HttpEntity<LinkInput> requestCreate = new HttpEntity<>(createDummyLink());
		return restTemplate.exchange(builder.toUriString(), HttpMethod.POST, requestCreate, LinkData.class).getBody();
	}

	private LinkInput createDummyLink() {
		LinkInput link = new LinkInput();
		link.setTitle(generateString(10));
		link.setUrl(RandomStringUtils.randomAlphabetic(30));
		return link;
	}

	private UUID createTag() {
		Tag tag = new Tag();
		tag.setName(generateString(10));
		return restTemplate.postForEntity(PREFIX_TAGS_API_URL, tag, Tag.class).getBody().getUuid();
	}

}
