package com.simbirsoft.simchat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.simchat.domain.dto.Role;
import com.simbirsoft.simchat.exception.RoleNotFoundException;
import com.simbirsoft.simchat.service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService service;

	@GetMapping(params = "id") // Read
	public Role getRoleById(@RequestParam("id") Long id) throws RoleNotFoundException {
		return service.getById(id);
	}

	@GetMapping("/all")
	public List<Role> getAll() throws RoleNotFoundException {
		return service.getAll();
	}
}
