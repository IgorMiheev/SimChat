package com.simbirsoft.simchat.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Roles {
	USER(new HashSet<>(Arrays.asList(Permission.DEVELOPER_READ))),
	ADMIN(new HashSet<>(Arrays.asList(Permission.DEVELOPER_READ, Permission.DEVELOPER_WRITE)));

	private Roles(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	private final Set<Permission> permissions;

	public Set<SimpleGrantedAuthority> getaAuthorities() {
		return getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toSet());
	}
}
