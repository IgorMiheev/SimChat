package com.simbirsoft.simchat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.simchat.domain.dto.Party;
import com.simbirsoft.simchat.domain.dto.PartyCreate;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.PartyAlreadyExistException;
import com.simbirsoft.simchat.exception.PartyNotFoundException;
import com.simbirsoft.simchat.exception.RoleNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.service.PartyService;

@RestController
@RequestMapping("/party")
public class PartyController {

	@Autowired
	private PartyService service;

	@PostMapping // Create
	public ResponseEntity<?> createParty(@RequestBody PartyCreate modelCreate)
			throws UsrNotFoundException, RoleNotFoundException, ChatNotFoundException, PartyAlreadyExistException {
		return service.create(modelCreate);
	}

	@GetMapping(params = "id") // Read
	public Party getPartyById(@RequestParam("id") Long id) throws PartyNotFoundException {
		return service.getById(id);
	}

	@PutMapping(params = "id") // Update
	public ResponseEntity<?> updateParty(@RequestParam("id") Long id, @RequestBody PartyCreate modelCreate)
			throws UsrNotFoundException, PartyNotFoundException, ChatNotFoundException {
		return service.update(id, modelCreate);
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@DeleteMapping(params = "id") // Delete
	public ResponseEntity<?> deletePartyById(@RequestParam("id") Long id) throws PartyNotFoundException {
		return service.delete(id);
	}

	@DeleteMapping(params = { "user_id", "chat_id" }) // Delete
	public String deletePartyByUserIdAndChatId(@RequestParam("user_id") Long user_id,
			@RequestParam("chat_id") Long chat_id)
			throws PartyNotFoundException, ChatNotFoundException, UsrNotFoundException {
		return service.deleteByUserAndChat(user_id, chat_id);
	}

	@GetMapping("/all")
	public List<Party> getAll() throws PartyNotFoundException {
		return service.getAll();
	}
}
