package com.simbirsoft.simchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.simchat.domain.dto.ChatBotCommand;
import com.simbirsoft.simchat.exception.AccessNotFoundException;
import com.simbirsoft.simchat.exception.ChatAlreadyExistException;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.PartyAlreadyExistException;
import com.simbirsoft.simchat.exception.PartyNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.service.ChatBotService;

@RestController
@RequestMapping("/chatbot")
public class ChatBotController {

	@Autowired
	ChatBotService chatBotService;

	@PostMapping // Parse
	public ResponseEntity parseCommand(@RequestBody ChatBotCommand chatBotCommand)
			throws UsrNotFoundException, ChatAlreadyExistException, ChatNotFoundException, PartyAlreadyExistException,
			PartyNotFoundException, AccessNotFoundException {
		return chatBotService.parseBotCommand(chatBotCommand);
	}
}
