package com.simbirsoft.simchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simbirsoft.simchat.domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
