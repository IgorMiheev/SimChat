package com.simbirsoft.simchat.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.enums.UserStatus;

public class SecurityUser implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4090404316871907327L;
	private final String username;
	private final String password;
	private final Boolean isActive;
	private final List<SimpleGrantedAuthority> authorities;

//	public SecurityUser(Long user_id, String username, String password, String email, Boolean is_banned,
//			Timestamp ban_endtime, List<SimpleGrantedAuthority> authorities) {
//		this.username = username;
//		this.password = password;
//		this.email = email;
//		this.is_banned = is_banned;
//		this.ban_endtime = ban_endtime;
//		this.authorities = authorities;
//	}

	public SecurityUser(String username, String password, Boolean isActive,
			List<SimpleGrantedAuthority> authorities) {
		this.username = username;
		this.password = password;
		this.isActive = isActive;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isActive;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isActive;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isActive;
	}

	@Override
	public boolean isEnabled() {
		return isActive;
	}

	public static UserDetails fromUsrEntity(UsrEntity usrEntity) {
		return new User(
				usrEntity.getUsername(),
				usrEntity.getPassword(),
				!usrEntity.getStatus().equals(UserStatus.DISABLED),
				!usrEntity.getStatus().equals(UserStatus.DISABLED),
				!usrEntity.getStatus().equals(UserStatus.DISABLED),
				!usrEntity.getStatus().equals(UserStatus.DISABLED),
				usrEntity.getAccess().getAuthorities());
	}
}
