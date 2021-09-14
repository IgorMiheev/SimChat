package com.simbirsoft.simchat.domain.dto;

import java.sql.Date;

import com.simbirsoft.simchat.domain.MessageEntity;

public class Message {

	private Long message_id;
	private Long user_id;
	private Long chat_id;
	private String content;
	private Date create_date;
	private int status;

	public static Message convertToDto(MessageEntity message) {
		Message messageDto = new Message(message.getMessage_id(), message.getUser().getUser_id(),
				message.getChat().getChat_id(), message.getContent(), message.getCreate_date(), message.getStatus());
		return messageDto;
	}

	public Message(Long message_id, Long user_id, Long chat_id, String content, Date create_date, int status) {
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

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
