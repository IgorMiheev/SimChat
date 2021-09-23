package com.simbirsoft.simchat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.simchat.domain.dto.Chat;
import com.simbirsoft.simchat.domain.dto.ChatCreate;
import com.simbirsoft.simchat.exception.ChatAlreadyExistException;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatService service;

	@PostMapping // Create
	public Chat createChat(@RequestBody ChatCreate modelCreate) throws UsrNotFoundException, ChatAlreadyExistException {
		return service.create(modelCreate);
	}

	@GetMapping(params = "id") // Read
	public Chat getChatById(@RequestParam("id") Long id) throws ChatNotFoundException {
		return service.getById(id);
	}

	@PutMapping(params = "id") // Update
	public Chat updateChat(@RequestParam("id") Long id, @RequestBody ChatCreate modelCreate)
			throws UsrNotFoundException, ChatNotFoundException {
		return service.update(id, modelCreate);
	}

	@DeleteMapping(params = "id") // Delete
	public String deleteChatById(@RequestParam("id") Long id) throws ChatNotFoundException {
		return service.delete(id);
	}

	@GetMapping("/all")
	public List<Chat> getAll() throws ChatNotFoundException {
		return service.getAll();
	}

}
