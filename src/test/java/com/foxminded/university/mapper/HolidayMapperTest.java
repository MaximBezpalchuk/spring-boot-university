package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dao.mapper.HolidayMapper;
import com.foxminded.university.dao.mapper.HolidayMapperImpl;
import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HolidayMapperTest {

    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private HolidayMapper holidayMapper = new HolidayMapperImpl(cathedraMapper);

    @Test
    void shouldMapHolidayToHolidayDto() {
        // given
        Cathedra cathedra = Cathedra.builder().id(1).build();
        Holiday holiday = Holiday.builder()
                .id(1)
                .name("Test Name")
                .date(LocalDate.of(2021, 1, 1))
                .cathedra(cathedra)
                .build();
        // when
        HolidayDto holidayDto = holidayMapper.holidayToDto(holiday);
        // then
        assertNotNull(holidayDto);
        assertEquals(holidayDto.getId(), 1);
        assertEquals(holidayDto.getName(), "Test Name");
        assertEquals(holidayDto.getDate(), LocalDate.of(2021, 1, 1));
        assertEquals(holidayDto.getCathedra().getId(), 1);
    }

    @Test
    void shouldMapHolidayDtoToHoliday() {
        // given
        CathedraDto cathedraDto = new CathedraDto(1, "Name");
        HolidayDto holidayDto = new HolidayDto(1, "Test Name", LocalDate.of(2021, 1, 1), cathedraDto);
        // when
        Holiday holiday = holidayMapper.dtoToHoliday(holidayDto);
        // then
        assertNotNull(holiday);
        assertEquals(holiday.getId(), 1);
        assertEquals(holiday.getName(), "Test Name");
        assertEquals(holiday.getDate(), LocalDate.of(2021, 1, 1));
        assertEquals(holiday.getCathedra().getId(), 1);
    }
}
