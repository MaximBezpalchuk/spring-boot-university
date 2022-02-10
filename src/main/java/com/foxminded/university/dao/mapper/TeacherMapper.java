package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.model.Teacher;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class, SubjectMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TeacherMapper {

    TeacherDto teacherToDto(Teacher teacher);

    Teacher dtoToTeacher(TeacherDto teacherDto);
}
