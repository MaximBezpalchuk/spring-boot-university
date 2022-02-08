package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.SubjectService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", imports = {Arrays.class, Collectors.class})
public abstract class TeacherMapper {

    @Autowired
    protected CathedraMapper cathedraMapper;
    @Autowired
    protected SubjectMapper subjectMapper;

    @Mapping(target = "cathedraDto", expression = "java(cathedraMapper.cathedraToDto(teacher.getCathedra()))")
    @Mapping(target = "subjectDtos", expression = "java(teacher.getSubjects().stream().map(subjectMapper::subjectToDto).collect(Collectors.toList()))")
    public abstract TeacherDto teacherToDto(Teacher teacher);

    @Mapping(target = "cathedra", expression = "java(cathedraMapper.dtoToCathedra(teacherDto.getCathedraDto()))")
    @Mapping(target = "subjects", expression = "java(teacherDto.getSubjectDtos().stream().map(subjectMapper::dtoToSubject).collect(Collectors.toList()))")
    public abstract Teacher dtoToTeacher(TeacherDto teacherDto);

    public abstract SubjectDto map(Subject subject);

    public abstract Subject map(SubjectDto subjectDto);
}
