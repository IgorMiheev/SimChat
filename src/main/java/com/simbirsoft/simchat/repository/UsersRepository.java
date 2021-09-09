package com.simbirsoft.simchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simbirsoft.simchat.domain.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

}
