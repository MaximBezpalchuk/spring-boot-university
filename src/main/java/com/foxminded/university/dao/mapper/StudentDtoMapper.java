package com.foxminded.university.dao.mapper;

import com.foxminded.university.model.Student;
import com.foxminded.university.model.dto.StudentDto;
import com.foxminded.university.service.GroupService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class StudentDtoMapper {

    public static StudentDtoMapper INSTANCE = Mappers.getMapper(StudentDtoMapper.class);
    @Autowired
    protected GroupService groupService;

    @Mapping(target = "id", source = "student.id")
    @Mapping(target = "firstName", source = "student.firstName")
    @Mapping(target = "lastName", source = "student.lastName")
    @Mapping(target = "phone", source = "student.phone")
    @Mapping(target = "address", source = "student.address")
    @Mapping(target = "email", source = "student.email")
    @Mapping(target = "gender", source = "student.gender")
    @Mapping(target = "postalCode", source = "student.postalCode")
    @Mapping(target = "education", source = "student.education")
    @Mapping(target = "birthDate", source = "student.birthDate")
    @Mapping(target = "groupName", source = "student.group.name")
    public abstract StudentDto studentToDto(Student student);

    @Mapping(target = "id", source = "studentDto.id")
    @Mapping(target = "firstName", source = "studentDto.firstName")
    @Mapping(target = "lastName", source = "studentDto.lastName")
    @Mapping(target = "phone", source = "studentDto.phone")
    @Mapping(target = "address", source = "studentDto.address")
    @Mapping(target = "email", source = "studentDto.email")
    @Mapping(target = "gender", source = "studentDto.gender")
    @Mapping(target = "postalCode", source = "studentDto.postalCode")
    @Mapping(target = "education", source = "studentDto.education")
    @Mapping(target = "birthDate", source = "studentDto.birthDate")
    @Mapping(target = "group", expression = "java(groupService.findByName(studentDto.getGroupName()))")
    public abstract Student dtoToStudent(StudentDto studentDto);
}
