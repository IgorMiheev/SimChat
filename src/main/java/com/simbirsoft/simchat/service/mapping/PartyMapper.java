package com.simbirsoft.simchat.service.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.simbirsoft.simchat.domain.PartyEntity;
import com.simbirsoft.simchat.domain.dto.Party;
import com.simbirsoft.simchat.domain.dto.PartyCreate;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.UsrRepository;

@Mapper(componentModel = "spring")
public abstract class PartyMapper {

	@Autowired
	protected UsrRepository usrRepository;

	@Autowired
	protected ChatRepository chatRepository;

	@Mapping(target = "user_id", expression = "java(entity.getUser().getUser_id())")
	@Mapping(target = "chat_id", expression = "java(entity.getChat().getChat_id())")
	public abstract Party toModel(PartyEntity entity);

	@Mapping(target = "user", expression = "java(usrRepository.findById(model.getUser_id()).orElse(null))")
	@Mapping(target = "chat", expression = "java(chatRepository.findById(model.getChat_id()).orElse(null))")
	public abstract PartyEntity toEntity(PartyCreate model);

	@Mapping(target = "user", expression = "java(usrRepository.findById(model.getUser_id()).orElse(null))")
	@Mapping(target = "chat", expression = "java(chatRepository.findById(model.getChat_id()).orElse(null))")
	public abstract PartyEntity updateEntity(PartyCreate model, @MappingTarget PartyEntity entity);

}
