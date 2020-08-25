package com.linksbox.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * This is for services offering a REST API.
 * 
 * The global exception handler that is responsible for returning a different json with a list of api-errors only. The
 * exceptions can be caught on client-side and the list of api-errors can be extracted using ApiErrors
 *
 * To activate this functionality, have a class extends from this one with @ControllerAdvice
 *
 */
@ControllerAdvice
public class RestApiExceptionHandler {

	/**
	 * whenever something seems wrong in the input data we send 400, at the current state even 404 (although that should
	 * be up for discussion)
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = { RestApiValidationException.class, IllegalArgumentException.class })
	protected ResponseEntity<Object> handleClientError(RestApiValidationException ex, WebRequest request) {

		return new ResponseEntity<>(ex.getApiErrors(), HttpHeaders.EMPTY, BAD_REQUEST);
	}

	/**
	 * Whenever something goes wrong on the server side we send 500.
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = { RestApiServerException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleInternalError(RestApiServerException ex, WebRequest request) {

		return new ResponseEntity<>(ex.getApiErrors(), HttpHeaders.EMPTY, INTERNAL_SERVER_ERROR);
	}
}
