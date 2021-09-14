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
@Table(name = "Message")
public class MessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long message_id;
	private String content;
	private int status;
	private Date create_date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UsersEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_id")
	private ChatEntity chat;

	public MessageEntity() {
	}

	public MessageEntity(Long message_id, String content, int status, Date create_date, UsersEntity user, ChatEntity chat) {
		this.message_id = message_id;
		this.content = content;
		this.status = status;
		this.create_date = create_date;
		this.user = user;
		this.chat = chat;
	}

	public void update(String content, int status, Date create_date, UsersEntity user, ChatEntity chat) {
		this.content = content;
		this.status = status;
		this.create_date = create_date;
		this.user = user;
		this.chat = chat;
	}

	public Long getMessage_id() {
		return message_id;
	}

	public void setMessage_id(Long message_id) {
		this.message_id = message_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
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

}
