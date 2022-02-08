package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.SubjectService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", imports = {Arrays.class, Collectors.class})
public abstract class TeacherMapper {

    public static TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);
    @Autowired
    protected SubjectService subjectService;
    @Autowired
    protected CathedraService cathedraService;

    @Mapping(target = "id", source = "teacher.id")
    @Mapping(target = "firstName", source = "teacher.firstName")
    @Mapping(target = "lastName", source = "teacher.lastName")
    @Mapping(target = "phone", source = "teacher.phone")
    @Mapping(target = "address", source = "teacher.address")
    @Mapping(target = "email", source = "teacher.email")
    @Mapping(target = "gender", source = "teacher.gender")
    @Mapping(target = "postalCode", source = "teacher.postalCode")
    @Mapping(target = "education", source = "teacher.education")
    @Mapping(target = "birthDate", source = "teacher.birthDate")
    @Mapping(target = "cathedraName", source = "teacher.cathedra.name")
    @Mapping(target = "degree", source = "teacher.degree")
    @Mapping(target = "subjectNames", expression = "java(teacher.getSubjects().stream().map(Subject::getName).collect(Collectors.toList()))")
    public abstract TeacherDto teacherToDto(Teacher teacher);

    @Mapping(target = "id", source = "teacherDto.id")
    @Mapping(target = "firstName", source = "teacherDto.firstName")
    @Mapping(target = "lastName", source = "teacherDto.lastName")
    @Mapping(target = "phone", source = "teacherDto.phone")
    @Mapping(target = "address", source = "teacherDto.address")
    @Mapping(target = "email", source = "teacherDto.email")
    @Mapping(target = "gender", source = "teacherDto.gender")
    @Mapping(target = "postalCode", source = "teacherDto.postalCode")
    @Mapping(target = "education", source = "teacherDto.education")
    @Mapping(target = "birthDate", source = "teacherDto.birthDate")
    @Mapping(target = "cathedra", expression = "java(cathedraService.findByName(teacherDto.getCathedraName()))")
    @Mapping(target = "degree", source = "teacherDto.degree")
    @Mapping(target = "subjects", expression = "java(teacherDto.getSubjectNames().stream().map(subjectService::findByName).collect(Collectors.toList()))")
    public abstract Teacher dtoToTeacher(TeacherDto teacherDto);

    public abstract SubjectDto map(Subject subject);

    public abstract Subject map(SubjectDto subjectDto);
}
