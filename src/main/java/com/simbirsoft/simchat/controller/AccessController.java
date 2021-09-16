package com.simbirsoft.simchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.simchat.domain.dto.Access;
import com.simbirsoft.simchat.domain.dto.AccessCreate;
import com.simbirsoft.simchat.exception.AccessNotFoundException;
import com.simbirsoft.simchat.exception.RoleNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.service.AccessService;

@RestController
@RequestMapping("/access")
public class AccessController {

	@Autowired
	private AccessService service;

	@PostMapping // Create
	public Access createAccess(@RequestBody AccessCreate modelCreate)
			throws UsrNotFoundException, RoleNotFoundException {
		return service.create(modelCreate);
	}

	@GetMapping(params = "id") // Read
	public Access getAccessById(@RequestParam("id") Long id) throws AccessNotFoundException {
		return service.getById(id);
	}

	@PutMapping(params = "id") // Update
	public Access updateAccess(@RequestParam("id") Long id, @RequestBody AccessCreate modelCreate)
			throws UsrNotFoundException, AccessNotFoundException {
		return service.update(id, modelCreate);
	}

	@DeleteMapping(params = "id") // Delete
	public String deleteAccessById(@RequestParam("id") Long id) throws AccessNotFoundException {
		return service.delete(id);
	}

}
