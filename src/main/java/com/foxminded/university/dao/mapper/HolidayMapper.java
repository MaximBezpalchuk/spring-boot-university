package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.CathedraService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class HolidayMapper {

    @Autowired
    protected CathedraService cathedraService;

    @Mapping(target = "cathedraName", source = "holiday.cathedra.name")
    public abstract HolidayDto holidayToDto(Holiday holiday);

    @Mapping(target = "cathedra", expression = "java(cathedraService.findByName(holidayDto.getCathedraName()))")
    public abstract Holiday dtoToHoliday(HolidayDto holidayDto);
}
