package com.linksbox.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Background: When an exception occurs in a rest service provided by on of our
 * microservices, the standard behaviour is to send an http status 400 or 500
 * and transport a list of ApiErrors in the payload of the response. The used
 * client side rest template will always throw a {@link RestClientException} in
 * those cases.
 * 
 * 
 * This utils can be used to extract the ApiErrors from the response.
 * 
 *
 */
@Slf4j
public class RestApiErrorUtils {

	private RestApiErrorUtils() {
	}

	public static List<ApiError> extractErrors(RestClientException exception) {
		ObjectMapper om = new ObjectMapper();
		HttpStatusCodeException httpStatusCodeException = (HttpStatusCodeException) exception;
		String errorResponse = httpStatusCodeException.getResponseBodyAsString();
		try {
			return om.readValue(errorResponse, new TypeReference<List<ApiError>>() {
			});
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	public static List<String> extractErrorMessages(RestClientException exception) {
		List<ApiError> errors = extractErrors(exception);
		List<String> errorsAsString = new ArrayList<>();
		for (ApiError error : errors) {
			errorsAsString.add(error.getMessage());
		}
		return errorsAsString;
	}

	public static String extractFirstErrorMessage(RestClientException exception) {

		List<String> errors = extractErrorMessages(exception);
		if (errors.isEmpty()) {
			return "";
		} else {
			return errors.get(0);
		}
	}
}
