package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.model.Audience;
import com.foxminded.university.service.CathedraService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AudienceMapper {

    @Autowired
    protected CathedraService cathedraService;

    @Mapping(target = "id", source = "audience.id")
    @Mapping(target = "room", source = "audience.room")
    @Mapping(target = "capacity", source = "audience.capacity")
    @Mapping(target = "cathedraName", source = "audience.cathedra.name")
    public abstract AudienceDto audienceToDto(Audience audience);

    @Mapping(target = "id", source = "audienceDto.id")
    @Mapping(target = "room", source = "audienceDto.room")
    @Mapping(target = "capacity", source = "audienceDto.capacity")
    @Mapping(target = "cathedra", expression = "java(cathedraService.findByName(audienceDto.getCathedraName()))")
    public abstract Audience dtoToAudience(AudienceDto audienceDto);
}
