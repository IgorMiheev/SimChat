package com.simbirsoft.simchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.simchat.domain.Users;
import com.simbirsoft.simchat.domain.dto.UsersDto;
import com.simbirsoft.simchat.exception.UserAlreadyExistException;
import com.simbirsoft.simchat.exception.UserNotExistException;
import com.simbirsoft.simchat.repository.UsersRepository;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UsersRepository usersRepository;

	@PostMapping // Create
	public ResponseEntity saveUser(@RequestBody Users user) {
		try {
			if (usersRepository.findById(user.getUser_id()).orElse(null) == null) {
				return ResponseEntity.ok(UsersDto.convertToDto(usersRepository.save(user)));
			}
			throw new UserAlreadyExistException("Пользователь с таким id уже существует");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping(params = "id") // Read
	public ResponseEntity getUserById(@RequestParam("id") Long user_id) {
		Users user = usersRepository.findById(user_id).orElse(null);
		try {
			if (user != null) {
				return ResponseEntity.ok(UsersDto.convertToDto(user));
			} else {
				throw new UserNotExistException("Пользователя с таким id не существует");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping // Update
	public ResponseEntity updateUser(@RequestBody Users user) {
		try {
			if (usersRepository.findById(user.getUser_id()).orElse(null) != null) {
				return ResponseEntity.ok(UsersDto.convertToDto(usersRepository.save(user)));
			}
			throw new UserNotExistException("Пользователя с таким id не существует");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping(params = "id") // Delete
	public ResponseEntity deleteUser(@RequestParam("id") Long user_id) {
		Users user = usersRepository.findById(user_id).orElse(null);
		try {
			if (user != null) {
				usersRepository.delete(user);
				return ResponseEntity.ok("Пользователь успешно удален");
			} else {
				throw new UserNotExistException("Пользователя с таким id не существует");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
