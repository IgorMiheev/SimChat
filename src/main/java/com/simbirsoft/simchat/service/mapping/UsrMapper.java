package com.simbirsoft.simchat.service.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.Usr;
import com.simbirsoft.simchat.domain.dto.UsrCreate;

@Mapper(componentModel = "spring")
public abstract class UsrMapper {

	public abstract Usr toModel(UsrEntity entity);

	public abstract UsrEntity toEntity(UsrCreate model);

	public abstract UsrEntity updateEntity(UsrCreate model, @MappingTarget UsrEntity entity);

}
