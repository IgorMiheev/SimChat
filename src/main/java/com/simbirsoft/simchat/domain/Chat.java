package com.simbirsoft.simchat.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Chat")
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chat_id;

	private String name;
	private String chat_type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users user;

	@OneToMany(mappedBy = "chat")
	private List<Message> messages;

	@OneToMany(mappedBy = "chat")
	private List<Party> partys;

	public Chat(Long chat_id, String name, String chat_type, Users user) {
		this.chat_id = chat_id;
		this.name = name;
		this.chat_type = chat_type;
		this.user = user;
	}

	public void update(String name, String chat_type, Users user) {
		this.name = name;
		this.chat_type = chat_type;
		this.user = user;
	}

	public List<Party> getPartys() {
		return partys;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public Chat() {
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

	public String getChat_type() {
		return chat_type;
	}

	public void setChat_type(String chat_type) {
		this.chat_type = chat_type;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}
