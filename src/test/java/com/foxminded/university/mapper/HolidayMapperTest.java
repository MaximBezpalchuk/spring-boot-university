package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dao.mapper.HolidayMapper;
import com.foxminded.university.dao.mapper.HolidayMapperImpl;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
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
        assertEquals(holidayDto.getCathedraDto().getId(), 1);
    }
}
