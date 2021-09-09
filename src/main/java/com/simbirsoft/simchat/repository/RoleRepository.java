package com.simbirsoft.simchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simbirsoft.simchat.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
