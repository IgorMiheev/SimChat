package com.simbirsoft.simchat.domain.dto;

import java.sql.Timestamp;

public class Message {

	private Long message_id;
	private Long user_id;
	private Long chat_id;
	private String content;
	private Timestamp create_date;
	private int status;

	public Message(Long message_id, Long user_id, Long chat_id, String content, Timestamp create_date, int status) {
		this.message_id = message_id;
		this.user_id = user_id;
		this.chat_id = chat_id;
		this.content = content;
		this.create_date = create_date;
		this.status = status;
	}

	public Long getMessage_id() {
		return message_id;
	}

	public void setMessage_id(Long message_id) {
		this.message_id = message_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getChat_id() {
		return chat_id;
	}

	public void setChat_id(Long chat_id) {
		this.chat_id = chat_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
