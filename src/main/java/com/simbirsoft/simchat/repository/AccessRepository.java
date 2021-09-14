package com.simbirsoft.simchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simbirsoft.simchat.domain.AccessEntity;

@Repository
public interface AccessRepository extends JpaRepository<AccessEntity, Long> {

}
