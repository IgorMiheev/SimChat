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

import com.simbirsoft.simchat.domain.dto.Message;
import com.simbirsoft.simchat.domain.dto.MessageCreate;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.MessageNotFoundException;
import com.simbirsoft.simchat.exception.RoleNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.service.MessageService;

@RestController
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private MessageService service;

	@PostMapping // Create
	public Message createMessage(@RequestBody MessageCreate modelCreate)
			throws UsrNotFoundException, RoleNotFoundException, ChatNotFoundException {
		return service.create(modelCreate);
	}

	@GetMapping(params = "id") // Read
	public Message getMessageById(@RequestParam("id") Long id) throws MessageNotFoundException {
		return service.getById(id);
	}

	@PutMapping(params = "id") // Update
	public Message updateMessage(@RequestParam("id") Long id, @RequestBody MessageCreate modelCreate)
			throws UsrNotFoundException, MessageNotFoundException, ChatNotFoundException {
		return service.update(id, modelCreate);
	}

	@PreAuthorize("hasAnyAuthority('Moderator','Administrator')")
	@DeleteMapping(params = "id") // Delete
	public String deleteMessageById(@RequestParam("id") Long id) throws MessageNotFoundException {
		return service.delete(id);
	}

	@PreAuthorize("hasAuthority('Administrator')")
	@GetMapping("/all")
	public List<Message> getAll() throws MessageNotFoundException {
		return service.getAll();
	}

	@GetMapping(path = "/all", params = "chat_id")
	public List<Message> getAllbyChatId(@RequestParam("chat_id") Long chat_id)
			throws MessageNotFoundException, ChatNotFoundException {
		return service.getAllbyChatId(chat_id);
	}
}
