package com.simbirsoft.simchat.service;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.PartyEntity;
import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.Chat;
import com.simbirsoft.simchat.domain.dto.ChatCreate;
import com.simbirsoft.simchat.domain.dto.PartyCreate;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.PartyRepository;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.service.mapping.ChatMapper;
import com.simbirsoft.simchat.service.mapping.PartyMapper;

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

	@Transactional
	public Chat create(ChatCreate modelCreate) throws UsrNotFoundException {
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}

		ChatEntity entity = mapper.toEntity(modelCreate);
		repository.save(entity);

		// Автоматически добавляем создателя чата в таблицу Party
		PartyCreate partyCreate = new PartyCreate(entity.getChat_id(), entity.getUser().getUser_id(), 0,
				new Timestamp(0L));
		PartyEntity party = partyMapper.toEntity(partyCreate);
		partyRepository.save(party);

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
