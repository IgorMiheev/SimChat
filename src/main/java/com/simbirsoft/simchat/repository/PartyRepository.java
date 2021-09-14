package com.simbirsoft.simchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simbirsoft.simchat.domain.PartyEntity;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Long> {

}
