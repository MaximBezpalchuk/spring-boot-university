package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.StudentDto;
import com.foxminded.university.model.Student;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {GroupMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StudentMapper {

    StudentDto studentToDto(Student student);

    Student dtoToStudent(StudentDto studentDto);
}
