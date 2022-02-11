package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.model.Holiday;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HolidayMapper {

    HolidayDto holidayToDto(Holiday holiday);

    Holiday dtoToHoliday(HolidayDto holidayDto);
}
