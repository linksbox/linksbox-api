package com.linksbox.exception;

import java.util.List;

/**
 * The exception for all serverside 50x codes
 *
 * TODO add constructor for passing throwables
 * 
 */
public class RestApiServerException extends RestApiException {


	private static final long serialVersionUID = 1L;

	public RestApiServerException(ApiError error) {
		super(error);
	}

	public RestApiServerException(String message) {
		super(message);
	}

	public RestApiServerException(List<String> messages){
		super(messages);
	}

	public RestApiServerException(ErrorKey key, String message){
		super(key, message);
	}

}
