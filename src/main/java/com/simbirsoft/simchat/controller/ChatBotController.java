package com.simbirsoft.simchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simbirsoft.simchat.domain.dto.ChatBotCommand;
import com.simbirsoft.simchat.service.ChatBotService;

@RestController
@RequestMapping("/chatbot")
public class ChatBotController {

	@Autowired
	ChatBotService chatBotService;

	@PostMapping // Parse
	public ResponseEntity parseCommand(@RequestBody ChatBotCommand chatBotCommand) throws Exception {
		return chatBotService.parseBotCommand(chatBotCommand);
	}
}
