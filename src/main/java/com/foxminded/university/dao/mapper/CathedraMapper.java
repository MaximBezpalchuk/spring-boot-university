package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.model.Cathedra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CathedraMapper {

    CathedraDto cathedraToDto(Cathedra cathedra);

    Cathedra dtoToCathedra(CathedraDto cathedraDto);
}
