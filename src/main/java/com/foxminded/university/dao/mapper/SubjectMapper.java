package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.model.Subject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SubjectMapper {

    @Mapping(target = "cathedraDto", source = "subject.cathedra")
    SubjectDto subjectToDto(Subject subject);

    @Mapping(target = "cathedra", source = "subjectDto.cathedraDto")
    Subject dtoToSubject(SubjectDto subjectDto);
}
