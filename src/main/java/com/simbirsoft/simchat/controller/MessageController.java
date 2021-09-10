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

import com.simbirsoft.simchat.domain.Chat;
import com.simbirsoft.simchat.domain.Message;
import com.simbirsoft.simchat.domain.Users;
import com.simbirsoft.simchat.domain.dto.MessageDto;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.MessageNotFoundException;
import com.simbirsoft.simchat.exception.UserNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.MessageRepository;
import com.simbirsoft.simchat.repository.UsersRepository;

@RestController
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private ChatRepository chatRepository;

	@PostMapping // Create
	public ResponseEntity createMessage(@RequestBody MessageDto messageDto) {
		Users user = usersRepository.findById(messageDto.getUser_id()).orElse(null);
		Chat chat = chatRepository.findById(messageDto.getChat_id()).orElse(null);
		try {
			if (user != null) {
				if (chat != null) {
					Message message = new Message(null, messageDto.getContent(), messageDto.getStatus(),
							messageDto.getCreate_date(), user, chat);
					return ResponseEntity.ok(MessageDto.convertToDto(messageRepository.save(message)));
				} else {
					throw new ChatNotFoundException("Чат с таким id не найден");
				}
			} else {
				throw new UserNotFoundException("Пользователь с таким id не найден");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping(params = "id") // Read
	public ResponseEntity getMessageById(@RequestParam("id") Long message_id) {
		Message message = messageRepository.findById(message_id).orElse(null);
		try {
			if (message != null) {
				return ResponseEntity.ok(MessageDto.convertToDto(message));
			} else {
				throw new MessageNotFoundException("Сообщение с таким id не найдено");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping // Update
	public ResponseEntity updateMessage(@RequestBody MessageDto messageDto) {
		Users user = usersRepository.findById(messageDto.getUser_id()).orElse(null);
		Chat chat = chatRepository.findById(messageDto.getChat_id()).orElse(null);
		Message message = messageRepository.findById(messageDto.getMessage_id()).orElse(null);
		try {
			if (message != null) {
				if (user != null) {
					if (chat != null) {
						message.update(messageDto.getContent(), messageDto.getStatus(), messageDto.getCreate_date(),
								user, chat);
						return ResponseEntity.ok(MessageDto.convertToDto(messageRepository.save(message)));
					} else {
						throw new ChatNotFoundException("Чат с таким id не найден");
					}
				} else {
					throw new UserNotFoundException("Пользователь с таким id не найден");
				}
			} else {
				throw new MessageNotFoundException("Сообщение с таким id не найдено");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping(params = "id") // Delete
	public ResponseEntity deleteMessageById(@RequestParam("id") Long message_id) {
		Message message = messageRepository.findById(message_id).orElse(null);
		try {
			if (message != null) {
				messageRepository.delete(message);
				return ResponseEntity.ok("Сообщение успешно удалено");
			} else {
				throw new MessageNotFoundException("Сообщение с таким id не найдено");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
