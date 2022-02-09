package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.model.Holiday;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HolidayMapper {

    @Mapping(target = "cathedraDto", source = "holiday.cathedra")
    HolidayDto holidayToDto(Holiday holiday);

    @Mapping(target = "cathedra", source = "holidayDto.cathedraDto")
    Holiday dtoToHoliday(HolidayDto holidayDto);
}
