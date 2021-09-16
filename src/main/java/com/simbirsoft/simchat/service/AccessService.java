package com.simbirsoft.simchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.AccessEntity;
import com.simbirsoft.simchat.domain.RoleEntity;
import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.Access;
import com.simbirsoft.simchat.domain.dto.AccessCreate;
import com.simbirsoft.simchat.exception.AccessNotFoundException;
import com.simbirsoft.simchat.exception.RoleNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.AccessRepository;
import com.simbirsoft.simchat.repository.RoleRepository;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.service.mapping.AccessMapper;

@Service
public class AccessService {

	@Autowired
	AccessMapper mapper;

	@Autowired
	private AccessRepository repository;

	@Autowired
	private UsrRepository usrRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Transactional
	public Access create(AccessCreate modelCreate) throws UsrNotFoundException, RoleNotFoundException {
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);
		RoleEntity roleEntity = roleRepository.findById(modelCreate.getRole_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (roleEntity == null) {
			throw new RoleNotFoundException("Роль с таким id не найдена");
		}

		AccessEntity entity = mapper.toEntity(modelCreate);
		repository.save(entity);
		return mapper.toModel(entity);
	}

	@Transactional(readOnly = true)
	public Access getById(Long id) throws AccessNotFoundException {
		AccessEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new AccessNotFoundException("Право доступа с таким id не найдено");
		}

		return mapper.toModel(entity);
	}

	@Transactional
	public Access update(Long id, AccessCreate modelCreate) throws UsrNotFoundException, AccessNotFoundException {
		AccessEntity entity = repository.findById(id).orElse(null);
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (entity == null) {
			throw new AccessNotFoundException("Право доступа с таким id не найдено");
		}

		entity = mapper.updateEntity(modelCreate, entity);
		repository.save(entity);
		return mapper.toModel(entity);
	}

	@Transactional
	public String delete(Long id) throws AccessNotFoundException {
		AccessEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new AccessNotFoundException("Право доступа с таким id не найдено");
		}

		repository.delete(entity);
		return new String("Право доступа успешно удалено");
	}

}
