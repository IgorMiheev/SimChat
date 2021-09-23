package com.simbirsoft.simchat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.MessageEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
	List<MessageEntity> findByChat(ChatEntity chat);
}
