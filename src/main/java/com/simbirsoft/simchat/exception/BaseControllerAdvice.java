package com.simbirsoft.simchat.exception;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.simbirsoft.simchat.security.JwtAutenticationException;

@ControllerAdvice
public class BaseControllerAdvice {
	private final DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

	@ExceptionHandler({ AccessNotFoundException.class, AccessNotFoundException.class, ChatNotFoundException.class,
			MessageNotFoundException.class, PartyNotFoundException.class, RoleNotFoundException.class,
			UsrNotFoundException.class, ParametersNotFoundException.class })
	public Object NotFoundException(Exception ex, WebRequest request) {
		return response(HttpStatus.NOT_FOUND, ex, request);

	}

	@ExceptionHandler({ ChatAlreadyExistException.class, UsrAlreadyExistException.class,
			PartyAlreadyExistException.class })
	public Object AlreadyExistException(Exception ex, WebRequest request) {
		return response(HttpStatus.CONFLICT, ex, request);
	}

	@ExceptionHandler({ org.springframework.http.converter.HttpMessageNotReadableException.class })
	public Object NotBody(Exception ex, WebRequest request) {
		return response(HttpStatus.NOT_FOUND, ex, request);
	}

	@ExceptionHandler({ JwtAutenticationException.class, AuthenticationException.class })
	public Object chatAutenticationException(Exception ex, WebRequest request) {
		return response(HttpStatus.UNAUTHORIZED, ex, request);
	}

	@ExceptionHandler({ Exception.class })
	public Object OtherException(Exception ex, WebRequest request) {
		return response(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
	}

	private Object response(HttpStatus status, Exception ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", isoDateTimeFormatter.format(ZonedDateTime.now()));
		body.put("status", status.toString());
		body.put("message", ex.getMessage());
		body.put("path", request.getDescription(false).replaceFirst("uri=", ""));
		ex.printStackTrace();
		return new ResponseEntity<>(body, headers, status);
	}

}
