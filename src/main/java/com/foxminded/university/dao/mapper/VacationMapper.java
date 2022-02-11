package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.VacationDto;
import com.foxminded.university.model.Vacation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VacationMapper {

    VacationDto vacationToDto(Vacation vacation);

    Vacation dtoToVacation(VacationDto vacationDto);
}