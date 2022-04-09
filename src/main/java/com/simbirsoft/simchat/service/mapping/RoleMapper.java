package com.simbirsoft.simchat.service.mapping;

import org.mapstruct.Mapper;

import com.simbirsoft.simchat.domain.RoleEntity;
import com.simbirsoft.simchat.domain.dto.Role;

@Mapper(componentModel = "spring")
public abstract class RoleMapper {

	public abstract Role toModel(RoleEntity entity);

}
