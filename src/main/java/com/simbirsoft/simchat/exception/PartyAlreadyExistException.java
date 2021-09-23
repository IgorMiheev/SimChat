package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class PartyAlreadyExistException extends Exception {
	public PartyAlreadyExistException(String message) {
		super(message);
	}

}
