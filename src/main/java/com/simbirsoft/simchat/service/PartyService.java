package com.simbirsoft.simchat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.PartyEntity;
import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.Party;
import com.simbirsoft.simchat.domain.dto.PartyCreate;
import com.simbirsoft.simchat.domain.enums.ChatType;
import com.simbirsoft.simchat.domain.enums.PartyStatus;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.PartyAlreadyExistException;
import com.simbirsoft.simchat.exception.PartyNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.PartyRepository;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.service.mapping.PartyMapper;
import com.simbirsoft.simchat.service.utils.CurrentUserRoleCheck;

@Service
public class PartyService {

	@Autowired
	PartyMapper mapper;

	@Autowired
	private PartyRepository repository;

	@Autowired
	private UsrRepository usrRepository;

	@Autowired
	private ChatRepository chatRepository;

	@Transactional
	public ResponseEntity<?> create(PartyCreate modelCreate)
			throws UsrNotFoundException, ChatNotFoundException, PartyAlreadyExistException {
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);
		ChatEntity chatEntity = chatRepository.findById(modelCreate.getChat_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		PartyEntity partyEntity = repository.findByUserAndChat(userEntity, chatEntity);
		if (partyEntity != null) {
			throw new PartyAlreadyExistException("Пользователь уже в чате");
		}

		// Проверка является ли текущий пользователь владельцем этого чата или
		// админом/модератором
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();

		if (chatEntity.getChat_type().equals(ChatType.PRIVATE)) {
			if (chatEntity.getUser().getUsername() != currentUserName) {
				if (!(CurrentUserRoleCheck.isAdministrator())) {
					if (!(CurrentUserRoleCheck.isModerator())) {
						return ResponseEntity.badRequest().body("У вас не хватает прав доступа для этой операции");
					}
				}
			}
		}

		PartyEntity entity = mapper.toEntity(modelCreate);
		repository.save(entity);
		return ResponseEntity
				.ok("Пользователь " + userEntity.getUsername() + " присоединился к чату " + chatEntity.getName());
	}

	@Transactional(readOnly = true)
	public Party getById(Long id) throws PartyNotFoundException {
		PartyEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new PartyNotFoundException("Участник чата с таким id не найден");
		}

		return mapper.toModel(entity);
	}

	@Transactional(readOnly = true)
	public java.util.List<Party> getAll() throws PartyNotFoundException {
		java.util.List<PartyEntity> entityList = repository.findAll();
		java.util.List<Party> resultList = new ArrayList<Party>();

		if (entityList.size() == 0) {
			throw new PartyNotFoundException("Участники чатов не найдены");
		}
		for (PartyEntity entity : entityList) {
			resultList.add(mapper.toModel(entity));
		}

		return resultList;
	}

	@Transactional
	public ResponseEntity<?> update(Long id, PartyCreate modelCreate)
			throws UsrNotFoundException, PartyNotFoundException, ChatNotFoundException {
		PartyEntity entity = repository.findById(id).orElse(null);
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);
		ChatEntity chatEntity = chatRepository.findById(modelCreate.getChat_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (entity == null) {
			throw new PartyNotFoundException("Участник чата с таким id не найден");
		}
		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		entity = mapper.updateEntity(modelCreate, entity);
		repository.save(entity);
		return ResponseEntity.ok(mapper.toModel(entity));
	}

	@Transactional
	public ResponseEntity<?> delete(Long id) throws PartyNotFoundException {
		PartyEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new PartyNotFoundException("Участник чата с таким id не найден");
		}

		// Проверка является ли текущий пользователь выходящим из чата, владельцем чата
		// или админом/модератором
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		if (entity.getUser().getUsername() != currentUserName) {
			if (entity.getChat().getUser().getUsername() != currentUserName) {
				if (!(CurrentUserRoleCheck.isAdministrator())) {
					if (!(CurrentUserRoleCheck.isModerator())) {
						return ResponseEntity.badRequest().body("У вас не хватает прав доступа для этой операции");
					}
				}
			}
		}

		repository.delete(entity);
		return ResponseEntity.ok("Готово. Пользователь " + entity.getUser().getUsername() + " отключен от чата "
				+ entity.getChat().getName());
	}

	@Transactional
	public String deleteByUserAndChat(Long user_id, Long chat_id)
			throws PartyNotFoundException, ChatNotFoundException, UsrNotFoundException {
		UsrEntity userEntity = usrRepository.findById(user_id).orElse(null);
		ChatEntity chatEntity = chatRepository.findById(chat_id).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		PartyEntity partyEntity = repository.findByUserAndChat(userEntity, chatEntity);
		if (partyEntity == null) {
			throw new PartyNotFoundException("Пользователь отсутствует в чате");
		}

		repository.delete(partyEntity);
		return new String("Участник чата успешно удален");
	}

	@Transactional
	public ResponseEntity<?> banUserByIdAndChatId(Long user_id, Long chat_id, Long banTime)
			throws UsrNotFoundException, ChatNotFoundException, PartyAlreadyExistException, PartyNotFoundException {
		UsrEntity userEntity = usrRepository.findById(user_id).orElse(null);
		ChatEntity chatEntity = chatRepository.findById(chat_id).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		PartyEntity partyEntity = repository.findByUserAndChat(userEntity, chatEntity);
		if (partyEntity == null) {
			PartyCreate partyCreate = new PartyCreate(chat_id, user_id, PartyStatus.BANNED_MEMBER,
					java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(banTime)));
			create(partyCreate);
			return ResponseEntity.ok("Готово. Пользователь " + userEntity.getUsername() + " не сможет войти в этот чат "
					+ banTime + " минут");
		} else {
			PartyCreate partyCreate = new PartyCreate(partyEntity.getChat().getChat_id(),
					partyEntity.getUser().getUser_id(), PartyStatus.BANNED_MEMBER,
					java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(banTime)));
			update(partyEntity.getParty_id(), partyCreate);
			return ResponseEntity.ok("Готово. Пользователь " + userEntity.getUsername() + " не сможет войти в этот чат "
					+ banTime + " минут");
		}

	}

}
