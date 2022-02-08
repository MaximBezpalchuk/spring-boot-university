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
    @Autowired
    protected CathedraMapper cathedraMapper;

    @Mapping(target = "cathedraDto", expression = "java(cathedraMapper.cathedraToDto(audience.getCathedra()))")
    public abstract AudienceDto audienceToDto(Audience audience);

    @Mapping(target = "cathedra", expression = "java(cathedraMapper.dtoToCathedra(audienceDto.getCathedraDto()))")
    public abstract Audience dtoToAudience(AudienceDto audienceDto);
}
