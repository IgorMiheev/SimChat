package com.simbirsoft.simchat.domain.dto;

import java.sql.Timestamp;

public class PartyCreate {
	private Long chat_id;
	private Long user_id;
	private String status;
	private Timestamp ban_endtime;

	public PartyCreate(Long chat_id, Long user_id, String status, Timestamp ban_endtime) {
		this.chat_id = chat_id;
		this.user_id = user_id;
		this.status = status;
		this.ban_endtime = ban_endtime;
	}

	public Long getChat_id() {
		return chat_id;
	}

	public void setChat_id(Long chat_id) {
		this.chat_id = chat_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getBan_endtime() {
		return ban_endtime;
	}

	public void setBan_endtime(Timestamp ban_endtime) {
		this.ban_endtime = ban_endtime;
	}

}
