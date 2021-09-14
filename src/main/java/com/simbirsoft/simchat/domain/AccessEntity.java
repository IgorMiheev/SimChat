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
public class AccessEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long access_id;

	@ManyToOne(targetEntity = UsersEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UsersEntity user;

	@ManyToOne(targetEntity = RoleEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private RoleEntity role;

	public AccessEntity() {
	}

	public AccessEntity(Long access_id, UsersEntity user, RoleEntity role) {
		this.access_id = access_id;
		this.user = user;
		this.role = role;
	}

	public void update(UsersEntity user, RoleEntity role) {
		this.user = user;
		this.role = role;
	}

	public Long getAccess_id() {
		return access_id;
	}

	public void setAccess_id(Long access_id) {
		this.access_id = access_id;
	}

	public UsersEntity getUser() {
		return user;
	}

	public void setUser(UsersEntity user) {
		this.user = user;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

}
