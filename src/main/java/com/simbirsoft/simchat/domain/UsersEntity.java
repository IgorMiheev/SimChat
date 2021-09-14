package com.simbirsoft.simchat.domain;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//used class name "Users" instead "User" because "user" - reserved name in PostgreSQL
@Entity
@Table(name = "Users")
public class UsersEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;

	private String username;
	private String password;
	private String email;
	private Boolean is_banned;
	private Date ban_endtime;

	@OneToMany(mappedBy = "user")
	private List<ChatEntity> chats;

	@OneToMany(mappedBy = "user")
	private List<AccessEntity> access;

	@OneToMany(mappedBy = "user")
	private List<MessageEntity> messages;

	@OneToMany(targetEntity = PartyEntity.class, mappedBy = "user")
	private Set<PartyEntity> party;

	public UsersEntity() {
	}

	public UsersEntity(Long user_id, String username, String password, String email, Boolean is_banned, Date ban_endtime) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.is_banned = is_banned;
		this.ban_endtime = ban_endtime;
	}

	public void update(String username, String password, String email, Boolean is_banned, Date ban_endtime) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.is_banned = is_banned;
		this.ban_endtime = ban_endtime;
	}

	public List<AccessEntity> getAccess() {
		return access;
	}

	public void setAccess(List<AccessEntity> access) {
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
