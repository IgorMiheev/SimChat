package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UsrAlreadyExistException extends Exception {

	public UsrAlreadyExistException(String message) {
		super(message);
	}

}
