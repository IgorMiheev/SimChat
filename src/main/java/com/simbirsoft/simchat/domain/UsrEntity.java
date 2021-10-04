package com.simbirsoft.simchat.domain;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.simbirsoft.simchat.domain.enums.UserStatus;

//used class name "Users" instead "User" because "user" - reserved name in PostgreSQL
@Entity
@Table(name = "Users")
@Cacheable(false)
public class UsrEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Column(nullable = false)
	private Timestamp ban_endtime;

	@OneToMany(mappedBy = "user")
	private List<ChatEntity> chats;

	@OneToOne(mappedBy = "user")
	private AccessEntity access;

	@OneToMany(mappedBy = "user")
	private List<MessageEntity> messages;

	@OneToMany(targetEntity = PartyEntity.class, mappedBy = "user")
	private Set<PartyEntity> party;

	public UsrEntity() {
	}

	public UsrEntity(Long user_id, String username, String password, String email, UserStatus status,
			Timestamp ban_endtime) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.status = status;
		this.ban_endtime = ban_endtime;
	}

	public void update(String username, String password, String email, UserStatus status, Timestamp ban_endtime) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.status = status;
		this.ban_endtime = ban_endtime;
	}

	public AccessEntity getAccess() {
		return access;
	}

	public void setAccess(AccessEntity access) {
		this.access = access;
	}

	public Set<PartyEntity> getParty() {
		return party;
	}

	public void setParty(Set<PartyEntity> party) {
		this.party = party;
	}

	public List<MessageEntity> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageEntity> messages) {
		this.messages = messages;
	}

	public List<ChatEntity> getChats() {
		return chats;
	}

	public void setChats(List<ChatEntity> chats) {
		this.chats = chats;
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
