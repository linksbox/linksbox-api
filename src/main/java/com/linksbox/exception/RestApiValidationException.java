package com.linksbox.exception;

import java.util.List;

/**
 * The exception for all 40x codes indicating there is some kind of a bad request
 */
public class RestApiValidationException extends RestApiException {

	private static final long serialVersionUID = 1L;

	public RestApiValidationException(ApiError error) {
		super(error);
	}

	public RestApiValidationException(String message) {
		super(message);
	}

	public RestApiValidationException(List<String> messages){
		super(messages);
	}

	public RestApiValidationException(ErrorKey key, String message){
		super(key, message);
	}

	public RestApiValidationException(ErrorKey key){
		super(key);
	}
}
