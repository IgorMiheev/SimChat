package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PartyNotFoundException extends Exception {
	public PartyNotFoundException(String message) {
		super(message);
	}

}
