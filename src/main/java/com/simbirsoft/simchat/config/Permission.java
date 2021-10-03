package com.simbirsoft.simchat.config;

public enum Permission {
	DEVELOPER_READ("DEVELOPER_READ"), DEVELOPER_WRITE("DEVELOPER_WRITE");

	private final String permission;

	private Permission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}

}
