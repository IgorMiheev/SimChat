package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AccessNotFoundException extends Exception {
	public AccessNotFoundException(String message) {
		super(message);

	}
}
