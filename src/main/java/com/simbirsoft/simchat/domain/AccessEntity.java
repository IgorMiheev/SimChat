package com.simbirsoft.simchat.domain;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "AccessTable")
public class AccessEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long access_id;

	@Column(insertable = false, updatable = false, unique = true, nullable = false)
	private Long user_id;

	@OneToOne(targetEntity = UsrEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private UsrEntity user;

	@ManyToOne(targetEntity = RoleEntity.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private RoleEntity role;

	public AccessEntity() {
	}

	public AccessEntity(Long access_id, UsrEntity user, RoleEntity role) {
		this.access_id = access_id;
		this.user = user;
		this.role = role;
	}

	public void update(UsrEntity user, RoleEntity role) {
		this.user = user;
		this.role = role;
	}

	public Long getAccess_id() {
		return access_id;
	}

	public void setAccess_id(Long access_id) {
		this.access_id = access_id;
	}

	public UsrEntity getUser() {
		return user;
	}

	public void setUser(UsrEntity user) {
		this.user = user;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public Set<SimpleGrantedAuthority> getAuthorities() {
		// return (Set<SimpleGrantedAuthority>) Arrays.asList(new
		// SimpleGrantedAuthority(getRole().getName()));
		/*
		 * System.out.println(Arrays.toString(Arrays.asList(getRole()).stream()
		 * .map(roles -> new SimpleGrantedAuthority(roles.getName()))
		 * .collect(Collectors.toSet())));
		 */
		// System.out.println(getRole().getName());
		return Arrays.asList(getRole()).stream().map(roles -> new SimpleGrantedAuthority(roles.getName()))
				.collect(Collectors.toSet());
	}

}
