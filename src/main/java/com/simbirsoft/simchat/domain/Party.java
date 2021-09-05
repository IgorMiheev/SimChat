package com.simbirsoft.simchat.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Party")
public class Party implements Serializable {

	@Id
	@ManyToOne(targetEntity = Users.class)
	@JoinColumn(name = "user_id")
	private Users users;

	@Id
	@ManyToOne
	@JoinColumn(name = "chat_id")
	private Chat chat;

	private int status;
	private Date ban_endtime;

	public Party() {
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getBan_endtime() {
		return ban_endtime;
	}

	public void setBan_endtime(Date ban_endtime) {
		this.ban_endtime = ban_endtime;
	}

}
