package com.simbirsoft.simchat.domain.dto;

import java.sql.Date;

import com.simbirsoft.simchat.domain.UsersEntity;

public class Users {
	private Long user_id;
	private String username;
	private String password;
	private String email;
	private Boolean is_banned;
	private Date ban_endtime;

	public static Users convertToDto(UsersEntity user) {
		Users usersDto = new Users(user.getUser_id(), user.getUsername(), user.getPassword(), user.getEmail(),
				user.getIs_banned(), user.getBan_endtime());
		return usersDto;
	}

	public Users(Long user_id, String username, String password, String email, Boolean is_banned, Date ban_endtime) {
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

	public Date getBan_endtime() {
		return ban_endtime;
	}

	public void setBan_endtime(Date ban_endtime) {
		this.ban_endtime = ban_endtime;
	}

}
