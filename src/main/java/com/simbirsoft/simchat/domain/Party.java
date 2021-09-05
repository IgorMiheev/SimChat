package com.simbirsoft.simchat.domain;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity

@IdClass(PartyID.class)
@Table(name = "Party")
public class Party {

	@Id
	@ManyToMany(targetEntity = Users.class)
	@JoinColumn(name = "user_id")
	private Set<Users> users;

	@ManyToOne
	@Id
	@JoinColumn(name = "chat_id")
	private Chat chat;

	private int status;
	private Date ban_endtime;

	public Party() {
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

	public Set<Users> getUsers() {
		return users;
	}

	public void setUsers(Set<Users> users) {
		this.users = users;
	}

}
