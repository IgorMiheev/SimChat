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

import com.simbirsoft.simchat.domain.RoleEntity;
import com.simbirsoft.simchat.domain.dto.Role;
import com.simbirsoft.simchat.exception.RoleNotFoundException;
import com.simbirsoft.simchat.repository.RoleRepository;

@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleRepository roleRepository;

	@PostMapping // Create
	public ResponseEntity createRole(@RequestBody Role roleDto) {
		try {
			RoleEntity role = new RoleEntity(null, roleDto.getName());
			return ResponseEntity.ok(Role.convertToDto(roleRepository.save(role)));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping(params = "id") // Read
	public ResponseEntity getRoleById(@RequestParam("id") Long role_id) {
		RoleEntity role = roleRepository.findById(role_id).orElse(null);
		try {
			if (role != null) {
				return ResponseEntity.ok(Role.convertToDto(role));
			} else {
				throw new RoleNotFoundException("Роль с таким id не найдена");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping // Update
	public ResponseEntity updateRole(@RequestBody Role roleDto) {
		RoleEntity role = roleRepository.findById(roleDto.getRole_id()).orElse(null);
		try {
			if (role != null) {
				role.update(roleDto.getName());
				return ResponseEntity.ok(Role.convertToDto(roleRepository.save(role)));
			}
			throw new RoleNotFoundException("Роль с таким id не найдена");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping(params = "id") // Delete
	public ResponseEntity deleteRoleById(@RequestParam("id") Long role_id) {
		RoleEntity role = roleRepository.findById(role_id).orElse(null);
		try {
			if (role != null) {
				roleRepository.delete(role);
				return ResponseEntity.ok("Роль успешно удалена");
			} else {
				throw new RoleNotFoundException("Роль с таким id не найдена");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
