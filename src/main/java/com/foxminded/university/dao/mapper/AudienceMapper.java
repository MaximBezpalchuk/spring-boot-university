package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.model.Audience;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AudienceMapper {

    @Mapping(target = "cathedraDto", source = "audience.cathedra")
    AudienceDto audienceToDto(Audience audience);

    @Mapping(target = "cathedra", source = "audienceDto.cathedraDto")
    Audience dtoToAudience(AudienceDto audienceDto);

}
