package com.simbirsoft.simchat.domain;

import java.util.List;

import javax.persistence.Column;
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
public class ChatEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chat_id;

	@Column(unique = true, nullable = false)
	private String name;
	@Column(nullable = false)
	private String chat_type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UsrEntity user;

	@OneToMany(mappedBy = "chat")
	private List<MessageEntity> messages;

	@OneToMany(mappedBy = "chat")
	private List<PartyEntity> partys;

	public ChatEntity(Long chat_id, String name, String chat_type, UsrEntity user) {
		this.chat_id = chat_id;
		this.name = name;
		this.chat_type = chat_type;
		this.user = user;
	}

	public void update(String name, String chat_type, UsrEntity user) {
		this.name = name;
		this.chat_type = chat_type;
		this.user = user;
	}

	public List<PartyEntity> getPartys() {
		return partys;
	}

	public List<MessageEntity> getMessages() {
		return messages;
	}

	public ChatEntity() {
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

	public UsrEntity getUser() {
		return user;
	}

	public void setUser(UsrEntity user) {
		this.user = user;
	}

}
