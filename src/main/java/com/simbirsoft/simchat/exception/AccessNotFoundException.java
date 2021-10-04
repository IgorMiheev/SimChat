package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AccessNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1253894622060048273L;

	public AccessNotFoundException(String message) {
		super(message);

	}
}
