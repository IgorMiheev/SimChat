package com.simbirsoft.simchat.exception;

//@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MessageNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8618486721701892106L;

	public MessageNotFoundException(String message) {
		super(message);
	}
}
