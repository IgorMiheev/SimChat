package com.simbirsoft.simchat.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "AccessTable")
public class Access {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long access_id;

	@ManyToOne(targetEntity = Users.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users user;

	@ManyToOne(targetEntity = Role.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private Role role;

	public Access() {
	}

	public Access(Long access_id, Users user, Role role) {
		this.access_id = access_id;
		this.user = user;
		this.role = role;
	}

	public void update(Users user, Role role) {
		this.user = user;
		this.role = role;
	}

	public Long getAccess_id() {
		return access_id;
	}

	public void setAccess_id(Long access_id) {
		this.access_id = access_id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
