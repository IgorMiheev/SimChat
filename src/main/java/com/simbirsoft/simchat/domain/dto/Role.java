package com.simbirsoft.simchat.domain.dto;

public class Role {
	private Long role_id;
	private String name;

	public Role(Long role_id, String name) {
		this.role_id = role_id;
		this.name = name;
	}

	public Long getRole_id() {
		return role_id;
	}

	public String getName() {
		return name;
	}

}
