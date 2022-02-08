package com.foxminded.university.dao.mapper;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.dto.CathedraDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class CathedraMapper {

    public static CathedraMapper INSTANCE = Mappers.getMapper(CathedraMapper.class);

    @Mapping(target = "id", source = "cathedra.id")
    @Mapping(target = "name", source = "cathedra.name")
    public abstract CathedraDto cathedraToDto(Cathedra cathedra);

    @Mapping(target = "id", source = "cathedraDto.id")
    @Mapping(target = "name", source = "cathedraDto.name")
    public abstract Cathedra dtoToCathedra(CathedraDto cathedraDto);
}
