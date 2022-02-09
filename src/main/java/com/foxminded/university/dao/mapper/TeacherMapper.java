package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.model.Teacher;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class, SubjectMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TeacherMapper {

    @Mapping(target = "cathedraDto", source = "teacher.cathedra")
    @Mapping(target = "subjectDtos", source = "teacher.subjects")
    TeacherDto teacherToDto(Teacher teacher);

    @Mapping(target = "cathedra", source = "teacherDto.cathedraDto")
    @Mapping(target = "subjects", source = "teacherDto.subjectDtos")
    Teacher dtoToTeacher(TeacherDto teacherDto);
}
