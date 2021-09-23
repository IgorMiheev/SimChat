package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ChatAlreadyExistException extends Exception {

	public ChatAlreadyExistException(String message) {
		super(message);
	}

}
