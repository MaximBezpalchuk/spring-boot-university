package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dao.mapper.HolidayMapper;
import com.foxminded.university.dao.mapper.HolidayMapperImpl;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class HolidaySystemTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private HolidayMapper holidayMapper = new HolidayMapperImpl(cathedraMapper);

    @Test
    public void whenGetAllHolidays_thenAllHolidaysReturned() throws Exception {
        Holiday holiday1 = createHolidayNoId();
        holiday1.setId(1);
        Holiday holiday2 = createHolidayNoId();
        holiday2.setId(2);
        holiday2.setName("Test Name2");
        List<Holiday> holidays = Arrays.asList(holiday1, holiday2);
        List<HolidayDto> holidayDtos = holidays.stream().map(holidayMapper::holidayToDto).collect(Collectors.toList());
        Page<HolidayDto> pageDtos = new PageImpl<>(holidayDtos, PageRequest.of(0, 3), 1);

        mockMvc.perform(get("/api/holidays")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetOneHoliday_thenOneHolidayReturned() throws Exception {
        Holiday holiday = createHolidayNoId();
        holiday.setId(1);
        HolidayDto holidayDto = holidayMapper.holidayToDto(holiday);

        mockMvc.perform(get("/api/holidays/{id}", holiday.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(holidayDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveHoliday_thenHolidaySaved() throws Exception {
        Holiday holiday = createHolidayNoId();
        holiday.setName("Any Name");
        HolidayDto holidayDto = holidayMapper.holidayToDto(holiday);

        mockMvc.perform(post("/api/holidays")
                .content(objectMapper.writeValueAsString(holidayDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/holidays/7"))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenEditHoliday_thenHolidayFound() throws Exception {
        Holiday holiday = createHolidayNoId();
        holiday.setId(1);
        HolidayDto holidayDto = holidayMapper.holidayToDto(holiday);

        mockMvc.perform(patch("/api/holidays/{id}", 1)
                .content(objectMapper.writeValueAsString(holidayDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteHoliday_thenHolidayDeleted() throws Exception {
        mockMvc.perform(delete("/api/holidays/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private Holiday createHolidayNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Holiday.builder().name("Test Name").date(LocalDate.of(2021, 1, 1)).cathedra(cathedra).build();
    }
}
