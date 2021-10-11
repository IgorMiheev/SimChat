package com.simbirsoft.simchat.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.simbirsoft.simchat.domain.enums.PartyStatus;

/**
 * 
 * field "status" can be: "member", "owner", "banned_member"
 * 
 * @author SNAZER
 */
@Entity
@Table(name = "PARTY")
public class PartyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long party_id;

	@ManyToOne(targetEntity = UsrEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UsrEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_id")
	private ChatEntity chat;

	@Enumerated(EnumType.STRING)
	@Column(name = "party_status", nullable = false)
	private PartyStatus party_status;

	@Column(name = "ban_endtime", nullable = false)
	private Timestamp ban_endtime;

	public PartyEntity() {
	}

	public PartyEntity(Long party_id, UsrEntity user, ChatEntity chat, PartyStatus status, Timestamp ban_endtime) {
		this.party_id = party_id;
		this.user = user;
		this.chat = chat;
		this.party_status = status;
		this.ban_endtime = ban_endtime;
	}

	public void update(UsrEntity user, ChatEntity chat, PartyStatus status, Timestamp ban_endtime) {
		this.user = user;
		this.chat = chat;
		this.party_status = status;
		this.ban_endtime = ban_endtime;
	}

	public Long getParty_id() {
		return party_id;
	}

	public void setParty_id(Long party_id) {
		this.party_id = party_id;
	}

	public UsrEntity getUser() {
		return user;
	}

	public void setUser(UsrEntity user) {
		this.user = user;
	}

	public ChatEntity getChat() {
		return chat;
	}

	public void setChat(ChatEntity chat) {
		this.chat = chat;
	}

	public PartyStatus getStatus() {
		return party_status;
	}

	public void setStatus(PartyStatus status) {
		this.party_status = status;
	}

	public Timestamp getBan_endtime() {
		return ban_endtime;
	}

	public void setBan_endtime(Timestamp ban_endtime) {
		this.ban_endtime = ban_endtime;
	}

}
