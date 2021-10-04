package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UsrNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4291069767292907476L;

	public UsrNotFoundException(String message) {
		super(message);
	}

}
