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

import com.simbirsoft.simchat.domain.AccessEntity;
import com.simbirsoft.simchat.domain.RoleEntity;
import com.simbirsoft.simchat.domain.UsersEntity;
import com.simbirsoft.simchat.domain.dto.Access;
import com.simbirsoft.simchat.exception.AccessNotFoundException;
import com.simbirsoft.simchat.exception.RoleNotFoundException;
import com.simbirsoft.simchat.exception.UserNotFoundException;
import com.simbirsoft.simchat.repository.AccessRepository;
import com.simbirsoft.simchat.repository.RoleRepository;
import com.simbirsoft.simchat.repository.UsersRepository;

@RestController
@RequestMapping("/access")
public class AccessController {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AccessRepository accessRepository;

	@PostMapping // Create
	public ResponseEntity createAccess(@RequestBody Access accessDto) {
		UsersEntity user = usersRepository.findById(accessDto.getUser_id()).orElse(null);
		RoleEntity role = roleRepository.findById(accessDto.getRole_id()).orElse(null);
		try {
			if (user != null) {
				if (role != null) {
					AccessEntity access = new AccessEntity(null, user, role);
					return ResponseEntity.ok(Access.convertToDto(accessRepository.save(access)));
				} else {
					throw new RoleNotFoundException("Роль с таким id не найдена");
				}
			} else {
				throw new UserNotFoundException("Пользователь с таким id не найден");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping(params = "id") // Read
	public ResponseEntity getAccessById(@RequestParam("id") Long access_id) {
		AccessEntity access = accessRepository.findById(access_id).orElse(null);
		try {
			if (access != null) {
				return ResponseEntity.ok(Access.convertToDto(access));
			} else {
				throw new AccessNotFoundException("Право доступа с таким id не найдено");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping // Update
	public ResponseEntity updateChat(@RequestBody Access accessDto) {
		UsersEntity user = usersRepository.findById(accessDto.getUser_id()).orElse(null);
		RoleEntity role = roleRepository.findById(accessDto.getRole_id()).orElse(null);
		AccessEntity access = accessRepository.findById(accessDto.getAccess_id()).orElse(null);
		try {
			if (access != null) {
				if (user != null) {
					if (role != null) {
						access.update(user, role);
						return ResponseEntity.ok(Access.convertToDto(accessRepository.save(access)));
					} else {
						throw new RoleNotFoundException("Роль с таким id не найдена");
					}
				} else {
					throw new UserNotFoundException("Пользователь с таким id не найден");
				}
			}
			throw new AccessNotFoundException("Право доступа с таким id не найдено");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping(params = "id") // Delete
	public ResponseEntity deleteAccessById(@RequestParam("id") Long access_id) {
		AccessEntity access = accessRepository.findById(access_id).orElse(null);
		try {
			if (access != null) {
				accessRepository.delete(access);
				return ResponseEntity.ok("Право доступа успешно удалено");
			} else {
				throw new AccessNotFoundException("Право доступа с таким id не найдено");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
