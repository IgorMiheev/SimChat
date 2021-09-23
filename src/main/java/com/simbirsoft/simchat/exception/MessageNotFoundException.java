package com.simbirsoft.simchat.exception;

//@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MessageNotFoundException extends Exception {
	public MessageNotFoundException(String message) {
		super(message);
	}
}
