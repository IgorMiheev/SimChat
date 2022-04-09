package com.simbirsoft.simchat.domain.dto;

import com.simbirsoft.simchat.domain.enums.ChatType;

public class ChatCreate {

	private String name;
	private Long user_id;
	private ChatType chat_type;

	public ChatCreate(String name, Long user_id, ChatType chat_type) {
		this.name = name;
		this.user_id = user_id;
		this.chat_type = chat_type;
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
