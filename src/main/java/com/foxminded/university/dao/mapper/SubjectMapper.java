package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.model.Subject;
import com.foxminded.university.service.CathedraService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class SubjectMapper {

    @Autowired
    protected CathedraService cathedraService;
    @Autowired
    protected CathedraMapper cathedraMapper;

    @Mapping(target = "cathedraDto", expression = "java(cathedraMapper.cathedraToDto(subject.getCathedra()))")
    public abstract SubjectDto subjectToDto(Subject subject);

    @Mapping(target = "cathedra", expression = "java(cathedraMapper.dtoToCathedra(subjectDto.getCathedraDto()))")
    public abstract Subject dtoToSubject(SubjectDto subjectDto);
}
