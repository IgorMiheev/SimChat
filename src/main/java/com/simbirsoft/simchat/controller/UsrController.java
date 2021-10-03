package com.simbirsoft.simchat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.simchat.domain.dto.Usr;
import com.simbirsoft.simchat.domain.dto.UsrCreate;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.service.UsrService;

@RestController
@RequestMapping("/user")
//@PreAuthorize("hasAuthority('Administrator')")
public class UsrController {

	@Autowired
	private UsrService service;

	@PostMapping // Create
	public Usr createUsr(@RequestBody UsrCreate modelCreate) throws Exception {
		return service.create(modelCreate);
	}

	@GetMapping(params = "id") // Read
	public Usr getUsrById(@RequestParam("id") Long id) throws UsrNotFoundException {
		return service.getById(id);
	}

	@PutMapping(params = "id") // Update
	public Usr updateUsr(@RequestParam("id") Long id, @RequestBody UsrCreate modelCreate) throws UsrNotFoundException {
		return service.update(id, modelCreate);
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@DeleteMapping(params = "id") // Delete
	public String deleteUsrById(@RequestParam("id") Long id) throws UsrNotFoundException {
		return service.delete(id);
	}

	@PreAuthorize("hasAnyAuthority('Moderator','Administrator')")
	@GetMapping("/all")
	public List<Usr> getAll() throws UsrNotFoundException {
		return service.getAll();
	}

	@PreAuthorize("hasAnyAuthority('Moderator','Administrator')")
	@PutMapping(params = { "id", "ban_time" })
	public Usr banUsr(@RequestParam("id") Long id, @RequestParam("ban_time") Long ban_time)
			throws UsrNotFoundException {
		return service.ban(id, ban_time);
	}

	@PreAuthorize("hasAnyAuthority('Moderator','Administrator')")
	@PutMapping(params = { "id", "unban" })
	public Usr banUsr(@RequestParam("id") Long id) throws UsrNotFoundException {
		return service.unBan(id);
	}

}
