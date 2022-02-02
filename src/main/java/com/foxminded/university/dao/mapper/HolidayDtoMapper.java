package com.foxminded.university.dao.mapper;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.model.dto.AudienceDto;
import com.foxminded.university.model.dto.HolidayDto;
import com.foxminded.university.service.CathedraService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class HolidayDtoMapper {

    public static HolidayDtoMapper INSTANCE = Mappers.getMapper(HolidayDtoMapper.class);
    @Autowired
    protected CathedraService cathedraService;

    @Mapping(target = "id", source = "holiday.id")
    @Mapping(target = "name", source = "holiday.name")
    @Mapping(target = "date", source = "holiday.date")
    @Mapping(target = "cathedraName", source = "holiday.cathedra.name")
    public abstract HolidayDto holidayToDto(Holiday holiday);

    @Mapping(target = "id", source = "holidayDto.id")
    @Mapping(target = "name", source = "holidayDto.name")
    @Mapping(target = "date", source = "holidayDto.date")
    @Mapping(target = "cathedra", expression = "java(cathedraService.findByName(holidayDto.getCathedraName()))")
    public abstract Holiday dtoToHoliday(HolidayDto holidayDto);
}
