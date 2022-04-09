package com.simbirsoft.simchat.service.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.dto.Chat;
import com.simbirsoft.simchat.domain.dto.ChatCreate;
import com.simbirsoft.simchat.repository.UsrRepository;

@Mapper(componentModel = "spring")
public abstract class ChatMapper {

	@Autowired
	protected UsrRepository usrRepository;

	@Mapping(target = "user_id", expression = "java(entity.getUser().getUser_id())")
	public abstract Chat toModel(ChatEntity entity);

	@Mapping(target = "user", expression = "java(usrRepository.findById(model.getUser_id()).orElse(null))")
	public abstract ChatEntity toEntity(ChatCreate model);

	@Mapping(target = "user", expression = "java(usrRepository.findById(model.getUser_id()).orElse(null))")
	public abstract ChatEntity updateEntity(ChatCreate model, @MappingTarget ChatEntity entity);

}
