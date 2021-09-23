package com.simbirsoft.simchat.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.Usr;
import com.simbirsoft.simchat.domain.dto.UsrCreate;
import com.simbirsoft.simchat.exception.UsrAlreadyExistException;
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
	public Usr create(UsrCreate modelCreate) throws UsrNotFoundException, Exception {
		UsrEntity usrEntity = repository.findByUsername(modelCreate.getUsername());
		if (usrEntity != null) {
			throw new UsrAlreadyExistException("Пользователь с таким username уже существует");
		}
		usrEntity = repository.findByEmail(modelCreate.getEmail());
		if (usrEntity != null) {
			throw new UsrAlreadyExistException("Пользователь с таким email уже существует");
		}

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

	/**
	 * Бан пользователя с id на ban_time минут
	 * 
	 * @param id       - user id
	 * @param ban_time - time in minutes
	 * @return сохраняет в бд и возвращает модель пользователя
	 * @throws UsrNotFoundException
	 * @throws UsrNotFoundException
	 */

	@Transactional
	public Usr ban(Long id, Long ban_time) throws UsrNotFoundException {
		UsrEntity entity = repository.findById(id).orElse(null);
		if (entity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}

		Timestamp ban_endtime = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(ban_time));
		entity.setIs_banned(true);
		entity.setBan_endtime(ban_endtime);
		repository.save(entity);
		return mapper.toModel(entity);
	}

	/**
	 * Снятие блокировки с пользователя
	 * 
	 * @param id
	 * @return
	 * @throws UsrNotFoundException
	 */
	@Transactional
	public Usr unBan(Long id) throws UsrNotFoundException {
		UsrEntity entity = repository.findById(id).orElse(null);
		if (entity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}

		Timestamp ban_endtime = java.sql.Timestamp.valueOf(LocalDateTime.now());
		entity.setIs_banned(false);
		entity.setBan_endtime(ban_endtime);
		repository.save(entity);
		return mapper.toModel(entity);
	}

}
