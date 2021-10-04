package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UsrAlreadyExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4466637980712501932L;

	public UsrAlreadyExistException(String message) {
		super(message);
	}

}
