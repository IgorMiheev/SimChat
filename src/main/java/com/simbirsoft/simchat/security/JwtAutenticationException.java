package com.simbirsoft.simchat.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class JwtAutenticationException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 642993643409615489L;
	private HttpStatus httpStatus;

	public JwtAutenticationException(String msg) {
		super(msg);
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public JwtAutenticationException(String msg, HttpStatus httpStatus) {
		super(msg);
		this.httpStatus = httpStatus;
	}

}
