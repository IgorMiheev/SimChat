package com.simbirsoft.simchat.service;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.MessageEntity;
import com.simbirsoft.simchat.domain.PartyEntity;
import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.Chat;
import com.simbirsoft.simchat.domain.dto.ChatCreate;
import com.simbirsoft.simchat.domain.dto.PartyCreate;
import com.simbirsoft.simchat.domain.enums.PartyStatus;
import com.simbirsoft.simchat.exception.ChatAlreadyExistException;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.MessageRepository;
import com.simbirsoft.simchat.repository.PartyRepository;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.service.mapping.ChatMapper;
import com.simbirsoft.simchat.service.mapping.PartyMapper;
import com.simbirsoft.simchat.service.utils.CurrentUserRoleCheck;
import com.simbirsoft.simchat.service.utils.CurrentUserStatusCheck;

@Service
public class ChatService {

	@Autowired
	ChatMapper mapper;

	@Autowired
	PartyMapper partyMapper;

	@Autowired
	private ChatRepository repository;

	@Autowired
	private UsrRepository usrRepository;

	@Autowired
	private PartyRepository partyRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Transactional
	public ResponseEntity<?> create(ChatCreate modelCreate) throws UsrNotFoundException, ChatAlreadyExistException {
		if (CurrentUserStatusCheck.isBanned()) {
			return ResponseEntity.badRequest().body("Вы забанены. Вам эта команда недоступна");
		}
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}

		ChatEntity chatEntity = repository.findByName(modelCreate.getName());
		if (chatEntity != null) {
			throw new ChatAlreadyExistException("Чат с таким названием уже существует");
		}

		if (modelCreate.getName().equals("")) {
			throw new ChatAlreadyExistException("Нельзя создать чат без имени");
		}

		ChatEntity entity = mapper.toEntity(modelCreate);
		repository.save(entity);

		// Автоматически добавляем создателя чата в таблицу Party как владельца
		PartyCreate partyCreate = new PartyCreate(entity.getChat_id(), entity.getUser().getUser_id(), PartyStatus.OWNER,
				new Timestamp(0L));
		PartyEntity party = partyMapper.toEntity(partyCreate);

		partyRepository.save(party);

		return ResponseEntity.ok("Чат с именем " + modelCreate.getName() + " успешно создан");
	}

	@Transactional(readOnly = true)
	public Chat getById(Long id) throws ChatNotFoundException {
		ChatEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		return mapper.toModel(entity);
	}

	@Transactional(readOnly = true)
	public java.util.List<Chat> getAll() throws ChatNotFoundException {
		java.util.List<ChatEntity> entityList = repository.findAll();
		java.util.List<Chat> resultList = new ArrayList<Chat>();

		if (entityList.size() == 0) {
			throw new ChatNotFoundException("Чаты не найдены");
		}
		for (ChatEntity entity : entityList) {
			resultList.add(mapper.toModel(entity));
		}

		return resultList;
	}

	@Transactional
	public ResponseEntity<?> update(Long id, ChatCreate modelCreate)
			throws UsrNotFoundException, ChatNotFoundException {
		ChatEntity entity = repository.findById(id).orElse(null);
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);
		if (CurrentUserStatusCheck.isBanned()) {
			return ResponseEntity.badRequest().body("Вы забанены. Вам эта команда недоступна");
		}

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (entity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		entity = mapper.updateEntity(modelCreate, entity);
		repository.save(entity);
		return ResponseEntity.ok(mapper.toModel(entity));
	}

	@Transactional
	public ResponseEntity<?> rename(Long id, String newChatName) throws UsrNotFoundException, ChatNotFoundException {
		if (CurrentUserStatusCheck.isBanned()) {
			return ResponseEntity.badRequest().body("Вы забанены. Вам эта команда недоступна");
		}
		ChatEntity currentEntity = repository.findById(id).orElse(null);
		ChatEntity newEntity = repository.findByName(newChatName);

		if (currentEntity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}
		if (newEntity != null) {
			throw new ChatNotFoundException("Чат с именем " + newChatName + " уже есть");
		}

		// Проверка является ли текущий пользователь владельцем чата
		// или админом
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		if (currentEntity.getUser().getUsername() != currentUserName) {
			if (!(CurrentUserRoleCheck.isAdministrator())) {
				return ResponseEntity.badRequest().body("У вас не хватает прав доступа для этой операции");
			}
		}

		currentEntity.setName(newChatName);
		repository.save(currentEntity);
		return ResponseEntity.ok(mapper.toModel(currentEntity));
	}

	@Transactional
	public ResponseEntity<?> delete(Long id) throws ChatNotFoundException, UsrNotFoundException {
		if (CurrentUserStatusCheck.isBanned()) {
			return ResponseEntity.badRequest().body("Вы забанены. Вам эта команда недоступна");
		}
		ChatEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		// Проверка является ли текущий пользователь владельцем чата
		// или админом
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		if (entity.getUser().getUsername() != currentUserName) {
			if (!(CurrentUserRoleCheck.isAdministrator())) {
				return ResponseEntity.badRequest().body("У вас не хватает прав доступа для этой операции");
			}
		}

		// сначала удаляем всех участников
		for (PartyEntity party : entity.getPartys()) {
			partyRepository.delete(party);
		}

		// удаляем все сообщения
		for (MessageEntity messageEntity : entity.getMessages()) {
			messageRepository.delete(messageEntity);
		}

		repository.delete(entity);

		return ResponseEntity.ok("Чат успешно удален");
	}

}
