package com.simbirsoft.simchat.service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.enums.UserStatus;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.UsrRepository;

@Component
public class CurrentUserStatusCheck {

	private static UsrRepository usrRepository;

	@Autowired
	public CurrentUserStatusCheck(UsrRepository usrRepository) {
		CurrentUserStatusCheck.usrRepository = usrRepository;
	}

	public static boolean isBanned() throws UsrNotFoundException {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		UsrEntity usrEntity = usrRepository.findByUsername(currentUserName).orElse(null);

		if (usrEntity == null) {
			throw new UsrNotFoundException("Пользователь " + currentUserName + " не найден");
		}
		return usrEntity.getStatus().equals(UserStatus.BANNED);
	}

	public static boolean isActive() throws UsrNotFoundException {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		UsrEntity usrEntity = usrRepository.findByUsername(currentUserName).orElse(null);

		if (usrEntity == null) {
			throw new UsrNotFoundException("Пользователь " + currentUserName + " не найден");
		}
		return usrEntity.getStatus().equals(UserStatus.ACTIVE);
	}

	public static boolean isDisabled() throws UsrNotFoundException {
		String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		UsrEntity usrEntity = usrRepository.findByUsername(currentUserName).orElse(null);

		if (usrEntity == null) {
			throw new UsrNotFoundException("Пользователь " + currentUserName + " не найден");
		}
		return usrEntity.getStatus().equals(UserStatus.DISABLED);
	}

}
