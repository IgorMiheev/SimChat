package com.simbirsoft.simchat.service.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.simbirsoft.simchat.domain.AccessEntity;
import com.simbirsoft.simchat.domain.dto.Access;
import com.simbirsoft.simchat.domain.dto.AccessCreate;
import com.simbirsoft.simchat.repository.RoleRepository;
import com.simbirsoft.simchat.repository.UsrRepository;

@Mapper(componentModel = "spring")
public abstract class AccessMapper {

	@Autowired
	protected UsrRepository usrRepository;

	@Autowired
	protected RoleRepository roleRepository;

	@Mapping(target = "user_id", expression = "java(entity.getUser().getUser_id())")
	@Mapping(target = "role_id", expression = "java(entity.getRole().getRole_id())")
	public abstract Access toModel(AccessEntity entity);

	@Mapping(target = "user", expression = "java(usrRepository.findById(model.getUser_id()).orElse(null))")
	@Mapping(target = "role", expression = "java(roleRepository.findById(model.getRole_id()).orElse(null))")
	public abstract AccessEntity toEntity(AccessCreate model);

	@Mapping(target = "user", expression = "java(usrRepository.findById(model.getUser_id()).orElse(null))")
	@Mapping(target = "role", expression = "java(roleRepository.findById(model.getRole_id()).orElse(null))")
	public abstract AccessEntity updateEntity(AccessCreate model, @MappingTarget AccessEntity entity);

}
