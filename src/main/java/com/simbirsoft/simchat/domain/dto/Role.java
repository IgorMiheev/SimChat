package com.simbirsoft.simchat.domain.dto;

import com.simbirsoft.simchat.domain.RoleEntity;

public class Role {
	private Long role_id;
	private String name;

	public static Role convertToDto(RoleEntity role) {
		Role roleDto = new Role(role.getRole_id(), role.getName());
		return roleDto;
	}

	public Role(Long role_id, String name) {
		this.role_id = role_id;
		this.name = name;
	}

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
