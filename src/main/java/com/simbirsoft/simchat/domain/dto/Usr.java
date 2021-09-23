package com.simbirsoft.simchat.domain.dto;

import java.sql.Timestamp;

public class Usr {
	private Long user_id;
	private String username;
	private String password;
	private String email;
	private Boolean is_banned;
	private Timestamp ban_endtime;

	public Usr(Long user_id, String username, String password, String email, Boolean is_banned, Timestamp ban_endtime) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.is_banned = is_banned;
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

	public Boolean getIs_banned() {
		return is_banned;
	}

	public void setIs_banned(Boolean is_banned) {
		this.is_banned = is_banned;
	}

	public Timestamp getBan_endtime() {
		return ban_endtime;
	}

	public void setBan_endtime(Timestamp ban_endtime) {
		this.ban_endtime = ban_endtime;
	}

}
