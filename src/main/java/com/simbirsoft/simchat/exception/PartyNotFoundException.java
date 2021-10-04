package com.simbirsoft.simchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PartyNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1057542220839248221L;

	public PartyNotFoundException(String message) {
		super(message);
	}

}
