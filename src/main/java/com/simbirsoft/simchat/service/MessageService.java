package com.simbirsoft.simchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.MessageEntity;
import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.Message;
import com.simbirsoft.simchat.domain.dto.MessageCreate;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.MessageNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.MessageRepository;
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

	@Transactional
	public Message create(MessageCreate modelCreate) throws UsrNotFoundException, ChatNotFoundException {
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);
		ChatEntity chatEntity = chatRepository.findById(modelCreate.getChat_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		MessageEntity entity = mapper.toEntity(modelCreate);
		repository.save(entity);
		return mapper.toModel(entity);
	}

	@Transactional(readOnly = true)
	public Message getById(Long id) throws MessageNotFoundException {
		MessageEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new MessageNotFoundException("Сообщение с таким id не найдено");
		}

		return mapper.toModel(entity);
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

}
