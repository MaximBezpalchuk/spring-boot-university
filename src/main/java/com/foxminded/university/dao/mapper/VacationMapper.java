package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.VacationDto;
import com.foxminded.university.model.Vacation;
import com.foxminded.university.service.TeacherService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class VacationMapper {

    public static VacationMapper INSTANCE = Mappers.getMapper(VacationMapper.class);
    @Autowired
    protected TeacherService teacherService;
    @Autowired
    protected TeacherMapper teacherMapper;

    @Mapping(target = "id", source = "vacation.id")
    @Mapping(target = "start", source = "vacation.start")
    @Mapping(target = "end", source = "vacation.end")
    @Mapping(target = "teacherDto", expression = "java(teacherMapper.teacherToDto(vacation.getTeacher()))")
    public abstract VacationDto vacationToDto(Vacation vacation);

    @Mapping(target = "id", source = "vacationDto.id")
    @Mapping(target = "start", source = "vacationDto.start")
    @Mapping(target = "end", source = "vacationDto.end")
    @Mapping(target = "teacher", expression = "java(teacherService.findByFirstNameAndLastNameAndBirthDate(vacationDto.getTeacherDto().getFirstName(), vacationDto.getTeacherDto().getLastName(), vacationDto.getTeacherDto().getBirthDate()))")
    public abstract Vacation dtoToVacation(VacationDto vacationDto);
}
