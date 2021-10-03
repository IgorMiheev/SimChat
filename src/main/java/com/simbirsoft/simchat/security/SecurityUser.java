package com.simbirsoft.simchat.security;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.simbirsoft.simchat.domain.UsrEntity;

public class SecurityUser implements UserDetails {

	private final Long user_id;
	private final String username;
	private final String password;
	private final String email;
	private final Boolean is_banned;
	private final Timestamp ban_endtime;
	private final List<SimpleGrantedAuthority> authorities;

	public SecurityUser(Long user_id, String username, String password, String email, Boolean is_banned,
			Timestamp ban_endtime, List<SimpleGrantedAuthority> authorities) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.is_banned = is_banned;
		this.ban_endtime = ban_endtime;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return is_banned;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return is_banned;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return is_banned;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return is_banned;
	}

	public static UserDetails fromUsrEntity(UsrEntity usrEntity) {
		return new User(
				usrEntity.getUsername(),
				usrEntity.getPassword(),
				usrEntity.getIs_banned(),
				usrEntity.getIs_banned(),
				usrEntity.getIs_banned(),
				usrEntity.getIs_banned(),
				usrEntity.getAccess().getAuthorities());
	}
}
