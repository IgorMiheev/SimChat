package com.simbirsoft.simchat.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.AccessEntity;
import com.simbirsoft.simchat.domain.RoleEntity;
import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.Access;
import com.simbirsoft.simchat.domain.dto.AccessCreate;
import com.simbirsoft.simchat.exception.AccessNotFoundException;
import com.simbirsoft.simchat.exception.RoleNotFoundException;
import com.simbirsoft.simchat.exception.UsrAlreadyExistException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.AccessRepository;
import com.simbirsoft.simchat.repository.RoleRepository;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.service.mapping.AccessMapper;
import com.simbirsoft.simchat.service.utils.CurrentUserRoleCheck;

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
	public Access create(AccessCreate modelCreate)
			throws UsrNotFoundException, RoleNotFoundException, UsrAlreadyExistException {
		UsrEntity userEntity = usrRepository.findById(modelCreate.getUser_id()).orElse(null);
		RoleEntity roleEntity = roleRepository.findById(modelCreate.getRole_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}
		if (roleEntity == null) {
			throw new RoleNotFoundException("Роль с таким id не найдена");
		}
		if (userEntity.getAccess() != null) {
			throw new UsrAlreadyExistException("Пользователю уже назначены права. Измените существующие");
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

	@Transactional(readOnly = true)
	public java.util.List<Access> getAll() throws AccessNotFoundException {
		java.util.List<AccessEntity> entityList = repository.findAll();
		java.util.List<Access> resultList = new ArrayList<Access>();

		if (entityList.size() == 0) {
			throw new AccessNotFoundException("Прав доступа не найдено");
		}
		for (AccessEntity entity : entityList) {
			resultList.add(mapper.toModel(entity));
		}

		return resultList;
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

	@Transactional
	public ResponseEntity<?> setModerator(Long id, Boolean moderator)
			throws UsrNotFoundException, AccessNotFoundException {

		// Проверка является ли текущий пользователь админом
		if (!(CurrentUserRoleCheck.isAdministrator())) {
			return ResponseEntity.badRequest().body("У вас не хватает прав доступа для этой операции");
		}

		RoleEntity moderatorRole = roleRepository.findById(2L).get();
		RoleEntity defaultUserRole = roleRepository.findById(3L).get();
		AccessEntity entity = repository.findById(id).orElse(null);

		if (entity == null) {
			throw new AccessNotFoundException("Право доступа с таким id не найдено");
		}

		UsrEntity userEntity = usrRepository.findById(entity.getUser().getUser_id()).orElse(null);

		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}

		if (moderator) {
			entity.setRole(moderatorRole);
		} else {
			entity.setRole(defaultUserRole);
		}

		repository.save(entity);

		if (moderator) {
			return ResponseEntity
					.ok("Готово. Пользователь с именем " + userEntity.getUsername() + " назначен модератором.");
		} else {
			return ResponseEntity
					.ok("Готово. Пользователь с именем " + userEntity.getUsername() + " больше не модератор.");

		}

	}
}
