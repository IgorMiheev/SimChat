package com.simbirsoft.simchat.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.Usr;
import com.simbirsoft.simchat.domain.dto.UsrCreate;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.service.mapping.UsrMapper;

@Service
public class UsrService {

	@Autowired
	UsrMapper mapper;

	@Autowired
	private UsrRepository repository;

	@Transactional
	public Usr create(UsrCreate modelCreate) throws UsrNotFoundException {

		UsrEntity entity = mapper.toEntity(modelCreate);
		repository.save(entity);
		return mapper.toModel(entity);
	}

	@Transactional(readOnly = true)
	public Usr getById(Long id) throws UsrNotFoundException {
		UsrEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}

		return mapper.toModel(entity);
	}

	@Transactional(readOnly = true)
	public java.util.List<Usr> getAll() throws UsrNotFoundException {
		java.util.List<UsrEntity> entityList = repository.findAll();
		java.util.List<Usr> resultList = new ArrayList<Usr>();

		if (entityList.size() == 0) {
			throw new UsrNotFoundException("Пользователи не найдены");
		}
		for (UsrEntity entity : entityList) {
			resultList.add(mapper.toModel(entity));
		}

		return resultList;
	}

	@Transactional
	public Usr update(Long id, UsrCreate modelCreate) throws UsrNotFoundException, UsrNotFoundException {
		UsrEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}

		entity = mapper.updateEntity(modelCreate, entity);
		repository.save(entity);
		return mapper.toModel(entity);
	}

	@Transactional
	public String delete(Long id) throws UsrNotFoundException {
		UsrEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}

		repository.delete(entity);
		return new String("Пользователь успешно удален");
	}

}
