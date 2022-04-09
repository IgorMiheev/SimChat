package com.simbirsoft.simchat.service.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserRoleCheck {

	public static boolean isAdministrator() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.contains(new SimpleGrantedAuthority("Administrator"));
	}

	public static boolean isModerator() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.contains(new SimpleGrantedAuthority("Moderator"));
	}

	public static boolean isDefaultUser() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.contains(new SimpleGrantedAuthority("DefaultUser"));
	}

}
