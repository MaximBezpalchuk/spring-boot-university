package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.model.Subject;
import com.foxminded.university.service.CathedraService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class SubjectDtoMapper {

    public static SubjectDtoMapper INSTANCE = Mappers.getMapper(SubjectDtoMapper.class);
    @Autowired
    protected CathedraService cathedraService;

    @Mapping(target = "id", source = "subject.id")
    @Mapping(target = "name", source = "subject.name")
    @Mapping(target = "description", source = "subject.description")
    @Mapping(target = "cathedraName", source = "subject.cathedra.name")
    public abstract SubjectDto subjectToDto(Subject subject);

    @Mapping(target = "id", source = "subjectDto.id")
    @Mapping(target = "name", source = "subjectDto.name")
    @Mapping(target = "description", source = "subjectDto.description")
    @Mapping(target = "cathedra", expression = "java(cathedraService.findByName(subjectDto.getCathedraName()))")
    public abstract Subject dtoToSubject(SubjectDto subjectDto);
}
