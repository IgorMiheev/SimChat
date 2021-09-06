package com.simbirsoft.simchat.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "AccessTable")
public class Access implements Serializable {

	@Id
	@ManyToOne(targetEntity = Users.class)
	@JoinColumn(name = "user_id")
	private Users user_acc;

	@Id
	@ManyToOne(targetEntity = Role.class)
	@JoinColumn(name = "role_id")
	private Role role;

	public Users getUser() {
		return user_acc;
	}

	public void setUser(Users user_acc) {
		this.user_acc = user_acc;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
