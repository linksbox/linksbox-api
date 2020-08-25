package com.linksbox.exception;

import java.io.Serializable;

import lombok.Getter;

/**
 * The standard error response has been decided to be List<ApiError> (similar to the graphQl implementation)
 *
 * The key is only necessary if the message can potentially be shown to the the frontend user.
 * In that case the key would be used as a localization key.
 */

@Getter
public class ApiError implements Serializable {

	private static final long serialVersionUID = 1L;

	private ErrorKey key = ErrorKey.NONE;
	private String message;

	public ApiError() {

	}

	public ApiError(String message) {
		this.message = message;
	}

	public ApiError(ErrorKey key) {
		this.message = key.name();
		this.key = key;
	}

	public ApiError(ErrorKey key, String message) {
		this.message = message;
		this.key = key;
	}

	@Override
	public String toString() {
		String msg = "";
		if(key != null){
			msg+= "key=" + key + " ";
		}
		return msg + "message=" + message;
	}
}
