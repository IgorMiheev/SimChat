package com.simbirsoft.simchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.Chat;
import com.simbirsoft.simchat.domain.dto.ChatCreate;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.service.mapping.ChatMapper;

@Service
public class ChatService {

	@Autowired
	ChatMapper mapper;

	@Autowired
	private ChatRepository repository;

	@Autowired
	private UsrRepository usrRepository;

	@Transactional
	public Chat create(ChatCreate modelCreate) throws UsrNotFoundException {
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}

		ChatEntity entity = mapper.toEntity(modelCreate);
		repository.save(entity);
		return mapper.toModel(entity);
	}

	@Transactional(readOnly = true)
	public Chat getById(Long id) throws ChatNotFoundException {
		ChatEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		return mapper.toModel(entity);
	}

	@Transactional
	public Chat update(Long id, ChatCreate modelCreate) throws UsrNotFoundException, ChatNotFoundException {
		ChatEntity entity = repository.findById(id).orElse(null);
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (entity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		entity = mapper.updateEntity(modelCreate, entity);
		repository.save(entity);
		return mapper.toModel(entity);
	}

	@Transactional
	public String delete(Long id) throws ChatNotFoundException {
		ChatEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new ChatNotFoundException("Чат с таким id не найден");
		}

		repository.delete(entity);
		return new String("Чат успешно удален");
	}

}