package com.simbirsoft.simchat.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
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
	@Column(nullable = false)
	private String content;
	@Column(nullable = false)
	private int status;

	@Column(nullable = false)
	// @CreatedDate
	private Timestamp create_date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UsrEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_id")
	private ChatEntity chat;

	public MessageEntity() {
	}

	public MessageEntity(Long message_id, String content, int status, Timestamp create_date, UsrEntity user,
			ChatEntity chat) {
		this.message_id = message_id;
		this.content = content;
		this.status = status;
		this.create_date = create_date;
		this.user = user;
		this.chat = chat;
	}

	public void update(String content, int status, Timestamp create_date, UsrEntity user, ChatEntity chat) {
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

	public Timestamp getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
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

}
