package com.simbirsoft.simchat.domain.dto;

import com.simbirsoft.simchat.domain.AccessEntity;

public class Access {

	private Long access_id;
	private Long user_id;
	private Long role_id;

	public static Access convertToDto(AccessEntity access) {
		Access accessDto = new Access(access.getAccess_id(), access.getUser().getUser_id(),
				access.getRole().getRole_id());
		return accessDto;
	}

	public Access(Long access_id, Long user_id, Long role_id) {
		this.access_id = access_id;
		this.user_id = user_id;
		this.role_id = role_id;
	}

	public Long getAccess_id() {
		return access_id;
	}

	public void setAccess_id(Long access_id) {
		this.access_id = access_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

}
