package com.simbirsoft.simchat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simbirsoft.simchat.domain.UsrEntity;

@Repository
public interface UsrRepository extends JpaRepository<UsrEntity, Long> {
	public UsrEntity findByEmail(String email);

	public Optional<UsrEntity> findByUsername(String username);

}
