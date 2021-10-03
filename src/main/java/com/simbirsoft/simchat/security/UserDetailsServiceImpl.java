package com.simbirsoft.simchat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.repository.UsrRepository;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsrRepository usrRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UsrEntity usrEntity = usrRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
		return SecurityUser.fromUsrEntity(usrEntity);
	}

}
