package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.StudentDto;
import com.foxminded.university.model.Student;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {GroupMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StudentMapper {

    @Mapping(target = "groupDto", source = "student.group")
    StudentDto studentToDto(Student student);

    @Mapping(target = "group", source = "studentDto.groupDto")
    Student dtoToStudent(StudentDto studentDto);
}
