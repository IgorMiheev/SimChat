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
import com.simbirsoft.simchat.domain.PartyEntity;
import com.simbirsoft.simchat.domain.UsersEntity;
import com.simbirsoft.simchat.domain.dto.Party;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.PartyNotFoundException;
import com.simbirsoft.simchat.exception.UserNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.PartyRepository;
import com.simbirsoft.simchat.repository.UsersRepository;

@RestController
@RequestMapping("/party")
public class PartyController {

	@Autowired
	private PartyRepository partyRepository;

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private UsersRepository usersRepository;

	@PostMapping // Create
	public ResponseEntity createParty(@RequestBody Party partyDto) {
		UsersEntity user = usersRepository.findById(partyDto.getUser_id()).orElse(null);
		ChatEntity chat = chatRepository.findById(partyDto.getChat_id()).orElse(null);
		try {
			if (user != null) {
				if (chat != null) {
					PartyEntity party = new PartyEntity(null, user, chat, partyDto.getStatus(), partyDto.getBan_endtime());
					return ResponseEntity.ok(Party.convertToDto(partyRepository.save(party)));
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
	public ResponseEntity getPartyById(@RequestParam("id") Long party_id) {
		PartyEntity party = partyRepository.findById(party_id).orElse(null);
		try {
			if (party != null) {
				return ResponseEntity.ok(Party.convertToDto(party));
			} else {
				throw new PartyNotFoundException("Участник чата с таким id не найден");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping // Update
	public ResponseEntity updateParty(@RequestBody Party partyDto) {
		UsersEntity user = usersRepository.findById(partyDto.getUser_id()).orElse(null);
		ChatEntity chat = chatRepository.findById(partyDto.getChat_id()).orElse(null);
		PartyEntity party = partyRepository.findById(partyDto.getParty_id()).orElse(null);
		try {
			if (party != null) {
				if (user != null) {
					if (chat != null) {
						party.update(user, chat, partyDto.getStatus(), partyDto.getBan_endtime());
						return ResponseEntity.ok(Party.convertToDto(partyRepository.save(party)));
					} else {
						throw new ChatNotFoundException("Чат с таким id не найден");
					}
				} else {
					throw new UserNotFoundException("Пользователь с таким id не найден");
				}
			} else {
				throw new PartyNotFoundException("Участник чата с таким id не найден");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping(params = "id") // Delete
	public ResponseEntity deletePartyById(@RequestParam("id") Long party_id) {
		PartyEntity party = partyRepository.findById(party_id).orElse(null);
		try {
			if (party != null) {
				partyRepository.delete(party);
				return ResponseEntity.ok("Участник чата успешно удален");
			} else {
				throw new PartyNotFoundException("Участник чата с таким id не найден");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
