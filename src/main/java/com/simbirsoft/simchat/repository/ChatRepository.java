package com.simbirsoft.simchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simbirsoft.simchat.domain.ChatEntity;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
	public ChatEntity findByName(String name);
}
