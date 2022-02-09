package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.VacationDto;
import com.foxminded.university.model.Vacation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VacationMapper {

    @Mapping(target = "teacherDto", source = "vacation.teacher")
    VacationDto vacationToDto(Vacation vacation);

    @Mapping(target = "teacher", source = "vacationDto.teacherDto")
    Vacation dtoToVacation(VacationDto vacationDto);
}