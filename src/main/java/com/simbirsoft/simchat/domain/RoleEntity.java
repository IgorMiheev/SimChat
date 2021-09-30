package com.simbirsoft.simchat.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Role")
public class RoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long role_id;
	@Column(unique = true, nullable = false)
	private String name;

	@OneToMany(mappedBy = "role")
	private List<AccessEntity> access;

	public RoleEntity() {
	}

	public RoleEntity(Long role_id, String name) {
		this.role_id = role_id;
		this.name = name;
	}

	public void update(String name) {
		this.name = name;
	}

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AccessEntity> getAccess() {
		return access;
	}

	public void setAccess(List<AccessEntity> access) {
		this.access = access;
	}

}
