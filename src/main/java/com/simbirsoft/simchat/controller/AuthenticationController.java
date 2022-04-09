package com.simbirsoft.simchat.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.simchat.domain.dto.AuthenticaionRequestDto;
import com.simbirsoft.simchat.domain.dto.RegisterUserDto;
import com.simbirsoft.simchat.exception.ParametersNotFoundException;
import com.simbirsoft.simchat.exception.RoleNotFoundException;
import com.simbirsoft.simchat.exception.UsrAlreadyExistException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.service.UsrService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private UsrService usrService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticaionRequestDto authenticaionRequestDto)
			throws ParametersNotFoundException {
		return usrService.login(authenticaionRequestDto);
	}

	@PostMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		usrService.logout(request, response);
	}

	@PostMapping("/register")
	public ResponseEntity<?> authenticate(@RequestBody RegisterUserDto registerUserDto)
			throws UsrNotFoundException, UsrAlreadyExistException, RoleNotFoundException, ParametersNotFoundException {
		return usrService.register(registerUserDto);
	}

}
