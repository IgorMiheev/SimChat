package com.simbirsoft.simchat.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.AuthenticaionRequestDto;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.security.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UsrRepository usrRepository;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticaionRequestDto authenticaionRequestDto) {
		try {

			// System.out.println(authenticaionRequestDto.getUserName());
			String username = authenticaionRequestDto.getUsername();
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, authenticaionRequestDto.getPassword()));
			UsrEntity usrEntity = usrRepository.findByUsername(username).orElseGet(null);
			if (usrEntity == null) {
				throw new UsernameNotFoundException("Пользователь не найден");
			}
			String token = jwtTokenProvider.createToken(username, usrEntity.getAccess().getRole().getName());
			Map<Object, Object> response = new HashMap<>();
			response.put("username", username);
			response.put("token", token);
			return ResponseEntity.ok(response);

		} catch (LockedException e) {

			return new ResponseEntity<>("Учетная запись заблокирована", HttpStatus.UNAUTHORIZED);

		} catch (AuthenticationException e) {
			// e.printStackTrace();
//			return ResponseEntity.ok(authenticaionRequestDto);
			return new ResponseEntity<>("Неверная комбинация логин/пароль", HttpStatus.FORBIDDEN);
		}

	}

	@PostMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
		securityContextLogoutHandler.logout(request, response, null);
	}
}
