package com.simbirsoft.simchat.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Party")
public class PartyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long party_id;

	@ManyToOne(targetEntity = UsersEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UsersEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_id")
	private ChatEntity chat;

	private int status;
	private Date ban_endtime;

	public PartyEntity() {
	}

	public PartyEntity(Long party_id, UsersEntity user, ChatEntity chat, int status, Date ban_endtime) {
		this.party_id = party_id;
		this.user = user;
		this.chat = chat;
		this.status = status;
		this.ban_endtime = ban_endtime;
	}

	public void update(UsersEntity user, ChatEntity chat, int status, Date ban_endtime) {
		this.user = user;
		this.chat = chat;
		this.status = status;
		this.ban_endtime = ban_endtime;
	}

	public Long getParty_id() {
		return party_id;
	}

	public void setParty_id(Long party_id) {
		this.party_id = party_id;
	}

	public UsersEntity getUser() {
		return user;
	}

	public void setUser(UsersEntity user) {
		this.user = user;
	}

	public ChatEntity getChat() {
		return chat;
	}

	public void setChat(ChatEntity chat) {
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
