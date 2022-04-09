package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7773479748088028557L;

	public RoleNotFoundException(String message) {
		super(message);
	}
}
