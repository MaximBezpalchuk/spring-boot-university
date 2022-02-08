package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.model.Cathedra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CathedraMapper {

    public abstract CathedraDto cathedraToDto(Cathedra cathedra);

    public abstract Cathedra dtoToCathedra(CathedraDto cathedraDto);
}
