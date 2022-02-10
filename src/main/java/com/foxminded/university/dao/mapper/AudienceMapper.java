package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.model.Audience;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AudienceMapper {

    AudienceDto audienceToDto(Audience audience);

    Audience dtoToAudience(AudienceDto audienceDto);
}
