package com.linksbox.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Exceptions thrown in our rest api endpoints.
 *
 */
public abstract class RestApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	private final List<ApiError> apiErrors = new ArrayList<>();

	public RestApiException(List<String> messages) {
		for (String message : messages) {
			apiErrors.add(new ApiError(message));
		}
	}

	public RestApiException(String message) {
		apiErrors.add(new ApiError(message));
	}

	public RestApiException(ApiError error) {
		apiErrors.add(error);
	}

	public RestApiException(ErrorKey key, String message) {
		apiErrors.add(new ApiError(key, message));
	}

	public RestApiException(ErrorKey key) {
		apiErrors.add(new ApiError(key));
	}
}
