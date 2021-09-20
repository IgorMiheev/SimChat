package com.simbirsoft.simchat.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.RoleEntity;
import com.simbirsoft.simchat.domain.dto.Role;
import com.simbirsoft.simchat.exception.RoleNotFoundException;
import com.simbirsoft.simchat.repository.RoleRepository;
import com.simbirsoft.simchat.service.mapping.RoleMapper;

@Service
public class RoleService {

	@Autowired
	RoleMapper mapper;

	@Autowired
	private RoleRepository repository;

	@Transactional(readOnly = true)
	public Role getById(Long id) throws RoleNotFoundException {
		RoleEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new RoleNotFoundException("Роль с таким id не найдена");
		}

		return mapper.toModel(entity);
	}

	@Transactional(readOnly = true)
	public java.util.List<Role> getAll() throws RoleNotFoundException {
		java.util.List<RoleEntity> entityList = repository.findAll();
		java.util.List<Role> resultList = new ArrayList<Role>();

		if (entityList.size() == 0) {
			throw new RoleNotFoundException("Роли не найдены");
		}
		for (RoleEntity entity : entityList) {
			resultList.add(mapper.toModel(entity));
		}

		return resultList;
	}

}
