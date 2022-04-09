package com.simbirsoft.simchat.domain.dto;

import java.sql.Timestamp;

import com.simbirsoft.simchat.domain.enums.UserStatus;

public class Usr {
	private Long user_id;
	private String username;
	private String password;
	private String email;
	private UserStatus status;
	private Timestamp ban_endtime;

	public Usr(Long user_id, String username, String password, String email, UserStatus status, Timestamp ban_endtime) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.status = status;
		this.ban_endtime = ban_endtime;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public Timestamp getBan_endtime() {
		return ban_endtime;
	}

	public void setBan_endtime(Timestamp ban_endtime) {
		this.ban_endtime = ban_endtime;
	}

}
