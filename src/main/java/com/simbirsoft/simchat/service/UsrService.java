package com.simbirsoft.simchat.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.AccessCreate;
import com.simbirsoft.simchat.domain.dto.AuthenticaionRequestDto;
import com.simbirsoft.simchat.domain.dto.RegisterUserDto;
import com.simbirsoft.simchat.domain.dto.Usr;
import com.simbirsoft.simchat.domain.dto.UsrCreate;
import com.simbirsoft.simchat.domain.enums.UserStatus;
import com.simbirsoft.simchat.exception.ParametersNotFoundException;
import com.simbirsoft.simchat.exception.RoleNotFoundException;
import com.simbirsoft.simchat.exception.UsrAlreadyExistException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.security.JwtTokenProvider;
import com.simbirsoft.simchat.service.mapping.UsrMapper;
import com.simbirsoft.simchat.service.utils.CurrentUserRoleCheck;

@Service
public class UsrService {

	@Autowired
	UsrMapper mapper;

	@Autowired
	private UsrRepository repository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AccessService accessService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public Usr create(UsrCreate modelCreate)
			throws UsrNotFoundException, UsrAlreadyExistException, RoleNotFoundException, ParametersNotFoundException {
		String email = modelCreate.getEmail();
		String username = modelCreate.getUsername();
		String password = modelCreate.getPassword();
		if (email == null || email.equals("") || username == null || username.equals("") || password == null
				|| password.equals("")) {
			throw new ParametersNotFoundException("Не все обязательные поля заполнены");
		}

		UsrEntity usrEntity = repository.findByUsername(modelCreate.getUsername()).orElse(null);
		if (usrEntity != null) {
			throw new UsrAlreadyExistException("Пользователь с таким username уже существует");
		}
		usrEntity = repository.findByEmail(modelCreate.getEmail());
		if (usrEntity != null) {
			throw new UsrAlreadyExistException("Пользователь с таким email уже существует");
		}

		modelCreate.setPassword(passwordEncoder.encode(modelCreate.getPassword()));
//		modelCreate.setPassword(modelCreate.getPassword());
		UsrEntity entity = mapper.toEntity(modelCreate);
		repository.save(entity);

		// Create Access DefaultUser(Role id=3)
		AccessCreate accessCreate = new AccessCreate(entity.getUser_id(), 3L);
		accessService.create(accessCreate);

		return mapper.toModel(entity);
	}

	@Transactional
	public ResponseEntity<?> register(RegisterUserDto registerUserDto)
			throws UsrNotFoundException, UsrAlreadyExistException, RoleNotFoundException, ParametersNotFoundException {
		UsrCreate usrCreate = new UsrCreate();
		usrCreate.setStatus(UserStatus.ACTIVE);
		usrCreate.setBan_endtime(new Timestamp(0L));
		usrCreate.setEmail(registerUserDto.getEmail());
		usrCreate.setPassword(registerUserDto.getPassword());
		usrCreate.setUsername(registerUserDto.getUsername());
		return ResponseEntity.ok(create(usrCreate));
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
		modelCreate.setPassword(passwordEncoder.encode(modelCreate.getPassword()));
//		modelCreate.setPassword(modelCreate.getPassword());
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
	public ResponseEntity<?> ban(Long id, Long ban_time) throws UsrNotFoundException {

		// Проверка является ли текущий пользователь админом/модератором
		if (!(CurrentUserRoleCheck.isAdministrator())) {
			if (!(CurrentUserRoleCheck.isModerator())) {
				return ResponseEntity.badRequest().body("У вас не хватает прав доступа для этой операции");
			}
		}

		UsrEntity entity = repository.findById(id).orElse(null);
		if (entity == null) {
			throw new UsrNotFoundException("Пользователь с таким id не найден");
		}

		Timestamp ban_endtime = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(ban_time));
		entity.setStatus(UserStatus.BANNED);
		entity.setBan_endtime(ban_endtime);
		repository.save(entity);
		return ResponseEntity
				.ok("Готово. Пользователь с именем " + entity.getUsername() + " забанен на " + ban_time + " минут(у).");
	}

	@Transactional
	public ResponseEntity<?> rename(String usrNameOld, String usrNameNew)
			throws UsrNotFoundException, UsrAlreadyExistException {

		UsrEntity usrEntityOld = repository.findByUsername(usrNameOld).orElse(null);
		UsrEntity usrEntityNew = repository.findByUsername(usrNameNew).orElse(null);

		if (usrEntityOld == null) {
			throw new UsrNotFoundException("Пользователь с именем " + usrNameOld + " не найден");
		}
		if (usrEntityNew != null) {
			throw new UsrAlreadyExistException("Пользователь с именем " + usrNameNew + " уже существует");
		}

		// Проверка является ли текущий пользователь владельцем этого чата или
		// админом/модератором
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!(usrEntityOld.getUsername().equals(currentUserName))) {
			if (!(CurrentUserRoleCheck.isAdministrator())) {
				return ResponseEntity.badRequest().body("У вас не хватает прав доступа для этой операции");
			}
		}

		usrEntityOld.setUsername(usrNameNew);
		repository.save(usrEntityOld);
		return ResponseEntity.ok("Готово. Пользователь с именем " + usrNameOld + " сменил имя на " + usrNameNew);
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
		entity.setStatus(UserStatus.ACTIVE);
		entity.setBan_endtime(ban_endtime);
		repository.save(entity);
		return mapper.toModel(entity);
	}

	/**
	 * @param authenticaionRequestDto
	 * @return
	 * @throws ParametersNotFoundException
	 */
	public ResponseEntity<?> login(AuthenticaionRequestDto authenticaionRequestDto) throws ParametersNotFoundException {
		try {
			if (authenticaionRequestDto == null) {
				throw new ParametersNotFoundException("Отсутсвуют данные логин/пароль");
			}

			String username = authenticaionRequestDto.getUsername();
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, authenticaionRequestDto.getPassword()));
			UsrEntity usrEntity = repository.findByUsername(username).orElseGet(null);
			if (usrEntity == null) {
				throw new UsernameNotFoundException("Пользователь не найден");
			}
			String token = jwtTokenProvider.createToken(username, usrEntity.getAccess().getRole().getName());
			Map<Object, Object> response = new HashMap<>();
			response.put("username", username);
			response.put("token", token);
			return ResponseEntity.ok(response);

		} catch (LockedException e) {
			return new ResponseEntity<>("Учетная запись заблокирована", HttpStatus.UNAUTHORIZED);
		} catch (AuthenticationException e) {
			return new ResponseEntity<>("Неверная комбинация логин/пароль", HttpStatus.FORBIDDEN);
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
		securityContextLogoutHandler.logout(request, response, null);
	}
}
