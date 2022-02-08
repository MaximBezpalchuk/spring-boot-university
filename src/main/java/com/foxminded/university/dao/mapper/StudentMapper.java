package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.StudentDto;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class StudentMapper {

    @Autowired
    protected GroupMapper groupMapper;

    @Mapping(target = "groupDto", expression = "java(groupMapper.groupToDto(student.getGroup()))")
    public abstract StudentDto studentToDto(Student student);

    @Mapping(target = "group", expression = "java(groupMapper.dtoToGroup(studentDto.getGroupDto()))")
    public abstract Student dtoToStudent(StudentDto studentDto);
}
