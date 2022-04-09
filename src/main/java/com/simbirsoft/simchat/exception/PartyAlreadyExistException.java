package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class PartyAlreadyExistException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2234859762751650367L;

	public PartyAlreadyExistException(String message) {
		super(message);
	}

}
