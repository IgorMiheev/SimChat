package com.simbirsoft.simchat.service;

import java.time.LocalDateTime;
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
import com.simbirsoft.simchat.domain.dto.Message;
import com.simbirsoft.simchat.domain.dto.MessageCreate;
import com.simbirsoft.simchat.domain.dto.MessageSended;
import com.simbirsoft.simchat.domain.enums.MessageStatus;
import com.simbirsoft.simchat.domain.enums.PartyStatus;
import com.simbirsoft.simchat.exception.AccessNotFoundException;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.MessageNotFoundException;
import com.simbirsoft.simchat.exception.PartyNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.MessageRepository;
import com.simbirsoft.simchat.repository.PartyRepository;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.service.mapping.MessageMapper;

@Service
public class MessageService {

	@Autowired
	MessageMapper mapper;

	@Autowired
	private MessageRepository repository;

	@Autowired
	private UsrRepository usrRepository;

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private PartyRepository partyRepository;

	@Autowired
	private PartyService partyService;

	@Transactional
	public Message create(MessageCreate modelCreate)
			throws UsrNotFoundException, ChatNotFoundException, PartyNotFoundException, AccessNotFoundException {
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);
		ChatEntity chatEntity = chatRepository.findById(modelCreate.getChat_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		// Проверка является ли текущий пользователь отправителем и
		// состоит ли он в этом чате
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!(userEntity.getUsername().equals(currentUserName))) {
			throw new AccessNotFoundException("Вы не можете отправлять сообщения от чужого имени");
		}
		PartyEntity partyEntity = partyRepository.findByUserAndChat(userEntity, chatEntity);
		if (partyEntity == null) {
			throw new ChatNotFoundException("Вы не состоите в этом чате");
		}
		if (partyEntity.getStatus().equals(PartyStatus.BANNED_MEMBER)) {
			if (partyEntity.getBan_endtime().before(java.sql.Timestamp.valueOf(LocalDateTime.now()))) {
				partyService.unbanUserByPartyId(partyEntity.getParty_id());
			} else {
				throw new AccessNotFoundException("Вы забанены и не можете отправлять сообщения");
			}
		}

		MessageEntity entity = mapper.toEntity(modelCreate);
		repository.save(entity);
		return mapper.toModel(entity);
	}

	@Transactional
	public Message sendMessageToChat(String text, Long chat_id)
			throws ChatNotFoundException, UsrNotFoundException, PartyNotFoundException, AccessNotFoundException {

		ChatEntity chatEntity = chatRepository.findById(chat_id).orElse(null);
		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		UsrEntity userEntity = usrRepository.findByUsername(currentUserName).orElse(null);

		MessageCreate messageCreate = new MessageCreate(
				userEntity.getUser_id(),
				chat_id,
				text,
				java.sql.Timestamp.valueOf(LocalDateTime.now()),
				MessageStatus.NORMAL);
		return create(messageCreate);
	}

	@Transactional(readOnly = true)
	public Message getById(Long id) throws MessageNotFoundException {
		MessageEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new MessageNotFoundException("Сообщение с таким id не найдено");
		}

		return mapper.toModel(entity);
	}

	@Transactional(readOnly = true)
	public java.util.List<Message> getAll() throws MessageNotFoundException {
		java.util.List<MessageEntity> entityList = repository.findAll();
		java.util.List<Message> resultList = new ArrayList<Message>();

		if (entityList.size() == 0) {
			throw new MessageNotFoundException("Сообщения не найдены");
		}
		for (MessageEntity entity : entityList) {
			resultList.add(mapper.toModel(entity));
		}

		return resultList;
	}

	@Transactional(readOnly = true)
	public ResponseEntity<?> getAllbyChatId(Long chat_id) throws MessageNotFoundException, ChatNotFoundException {
		ChatEntity chatEntity = chatRepository.findById(chat_id).orElse(null);

		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		// Проверка состоит ли текущий пользователь в этом чате
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		UsrEntity userEntity = usrRepository.findByUsername(currentUserName).orElse(null);
		PartyEntity partyEntity = partyRepository.findByUserAndChat(userEntity, chatEntity);
		if (partyEntity == null) {
			return ResponseEntity.badRequest().body("Вы не состоите в этом чате");
		}

		java.util.List<MessageEntity> entityList = repository.findByChat(chatEntity);
		java.util.List<Message> resultList = new ArrayList<Message>();

		if (entityList.size() == 0) {
			throw new MessageNotFoundException("Сообщения не найдены");
		}
		for (MessageEntity entity : entityList) {
			resultList.add(mapper.toModel(entity));
		}

		return ResponseEntity.ok(resultList);
	}

	@Transactional
	public Message update(Long id, MessageCreate modelCreate)
			throws UsrNotFoundException, MessageNotFoundException, ChatNotFoundException {
		MessageEntity entity = repository.findById(id).orElse(null);
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);
		ChatEntity chatEntity = chatRepository.findById(modelCreate.getChat_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (entity == null) {
			throw new MessageNotFoundException("Сообщение с таким id не найдено");
		}
		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		entity = mapper.updateEntity(modelCreate, entity);
		repository.save(entity);
		return mapper.toModel(entity);
	}

	@Transactional
	public String delete(Long id) throws MessageNotFoundException {
		MessageEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new MessageNotFoundException("Сообщение с таким id не найдено");
		}

		repository.delete(entity);
		return new String("Сообщение успешно удалено");
	}

	@Transactional
	public MessageSended send(MessageSended messageSended)
			throws UsrNotFoundException, ChatNotFoundException, PartyNotFoundException, AccessNotFoundException {
		UsrEntity usrEntity = usrRepository.findByUsername(messageSended.getUsername()).orElse(null);
		if (usrEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким именем не найден");
		}
		MessageCreate messageCreate = new MessageCreate();
		messageCreate.setChat_id(messageSended.getChat_id());
		messageCreate.setContent(messageSended.getContent());
		messageCreate.setCreate_date(messageSended.getCreate_date());
		messageCreate.setStatus(messageSended.getStatus());
		messageCreate.setUser_id(usrEntity.getUser_id());

		create(messageCreate);

		return messageSended;
	}

}
