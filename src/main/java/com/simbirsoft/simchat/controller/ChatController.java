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

import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.UsersEntity;
import com.simbirsoft.simchat.domain.dto.Chat;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.UserNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.UsersRepository;

@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private UsersRepository usersRepository;

	@PostMapping // Create
	public ResponseEntity createChat(@RequestBody Chat chatDto) {
		UsersEntity user = usersRepository.findById(chatDto.getUser_id()).orElse(null);
		try {
			if (user != null) {
				ChatEntity chat = new ChatEntity(null, chatDto.getName(), chatDto.getChat_type(), user);
				return ResponseEntity.ok(Chat.convertToDto(chatRepository.save(chat)));
			} else {
				throw new UserNotFoundException("Пользователь с таким id не найден");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping(params = "id") // Read
	public ResponseEntity getChatById(@RequestParam("id") Long chat_id) {
		ChatEntity chat = chatRepository.findById(chat_id).orElse(null);
		try {
			if (chat != null) {
				return ResponseEntity.ok(Chat.convertToDto(chat));
			} else {
				throw new ChatNotFoundException("Чат с таким id не найден");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping // Update
	public ResponseEntity updateChat(@RequestBody Chat chatDto) {
		UsersEntity user = usersRepository.findById(chatDto.getUser_id()).orElse(null);
		ChatEntity chat = chatRepository.findById(chatDto.getChat_id()).orElse(null);
		try {
			if (chat != null) {
				if (user != null) {
					chat.update(chatDto.getName(), chatDto.getChat_type(), user);
					return ResponseEntity.ok(Chat.convertToDto(chatRepository.save(chat)));
				} else {
					throw new UserNotFoundException("Пользователь с таким id не найден");
				}
			}
			throw new ChatNotFoundException("Чат с таким id не найден");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping(params = "id") // Delete
	public ResponseEntity deleteChatById(@RequestParam("id") Long chat_id) {
		ChatEntity chat = chatRepository.findById(chat_id).orElse(null);
		try {
			if (chat != null) {
				chatRepository.delete(chat);
				return ResponseEntity.ok("Чат успешно удален");
			} else {
				throw new ChatNotFoundException("Чат с таким id не найден");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
