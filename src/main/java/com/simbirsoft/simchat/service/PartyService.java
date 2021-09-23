package com.simbirsoft.simchat.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.PartyEntity;
import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.Party;
import com.simbirsoft.simchat.domain.dto.PartyCreate;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.PartyAlreadyExistException;
import com.simbirsoft.simchat.exception.PartyNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.PartyRepository;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.service.mapping.PartyMapper;

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
	public Party create(PartyCreate modelCreate)
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

		PartyEntity entity = mapper.toEntity(modelCreate);
		repository.save(entity);
		return mapper.toModel(entity);
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
	public Party update(Long id, PartyCreate modelCreate)
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
		return mapper.toModel(entity);
	}

	@Transactional
	public String delete(Long id) throws PartyNotFoundException {
		PartyEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new PartyNotFoundException("Участник чата с таким id не найден");
		}

		repository.delete(entity);
		return new String("Участник чата успешно удален");
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

}
