package com.simbirsoft.simchat.domain.dto;

import com.simbirsoft.simchat.domain.enums.ChatType;

public class Chat {

	private Long chat_id;
	private String name;
	private Long user_id;
	private ChatType chat_type;

	public Chat(Long chat_id, String name, Long user_id, ChatType chat_type) {
		this.chat_id = chat_id;
		this.name = name;
		this.user_id = user_id;
		this.chat_type = chat_type;
	}

	public Long getChat_id() {
		return chat_id;
	}

	public void setChat_id(Long chat_id) {
		this.chat_id = chat_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public ChatType getChat_type() {
		return chat_type;
	}

	public void setChat_type(ChatType chat_type) {
		this.chat_type = chat_type;
	}

}
