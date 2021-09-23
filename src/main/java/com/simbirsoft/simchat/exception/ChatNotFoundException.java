package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ChatNotFoundException extends Exception {

	public ChatNotFoundException(String message) {
		super(message);
	}

}
