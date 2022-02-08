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
    @Autowired
    protected CathedraMapper cathedraMapper;

    @Mapping(target = "cathedraDto", expression = "java(cathedraMapper.cathedraToDto(holiday.getCathedra()))")
    public abstract HolidayDto holidayToDto(Holiday holiday);

    @Mapping(target = "cathedra", expression = "java(cathedraService.findById(holidayDto.getCathedraDto().getId()))")
    public abstract Holiday dtoToHoliday(HolidayDto holidayDto);
}
