package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.model.Cathedra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CathedraMapper {

    @Mapping(target = "id", source = "cathedra.id")
    @Mapping(target = "name", source = "cathedra.name")
    public abstract CathedraDto cathedraToDto(Cathedra cathedra);

    @Mapping(target = "id", source = "cathedraDto.id")
    @Mapping(target = "name", source = "cathedraDto.name")
    public abstract Cathedra dtoToCathedra(CathedraDto cathedraDto);
}
