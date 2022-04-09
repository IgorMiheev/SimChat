package com.simbirsoft.simchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.PartyEntity;
import com.simbirsoft.simchat.domain.UsrEntity;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Long> {
	PartyEntity findByUserAndChat(UsrEntity user, ChatEntity chat);
}
