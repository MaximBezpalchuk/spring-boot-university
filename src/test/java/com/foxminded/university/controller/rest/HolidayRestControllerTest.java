package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dao.mapper.HolidayMapper;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class HolidayRestControllerTest {


    private MockMvc mockMvc;
    private final HolidayMapper holidayMapper = Mappers.getMapper(HolidayMapper .class);
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper .class);
    ObjectMapper objectMapper;
    @Mock
    private HolidayService holidayService;
    @Mock
    private CathedraService cathedraService;
    @InjectMocks
    private HolidayRestController holidayRestController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(holidayRestController).setCustomArgumentResolvers(resolver).build();
        ReflectionTestUtils.setField(holidayRestController, "holidayMapper", holidayMapper);
        ReflectionTestUtils.setField(holidayMapper, "cathedraService", cathedraService);
        ReflectionTestUtils.setField(holidayMapper, "cathedraMapper", cathedraMapper);
    }

    @Test
    public void whenGetAllHolidays_thenAllHolidaysReturned() throws Exception {
        Holiday holiday1 = createHolidayNoId();
        holiday1.setId(1);
        Holiday holiday2 = createHolidayNoId();
        holiday2.setId(2);
        List<Holiday> holidays = Arrays.asList(holiday1, holiday2);
        Page<Holiday> page = new PageImpl<>(holidays, PageRequest.of(0, 1), 2);
        List<HolidayDto> holidayDtos = holidays.stream().map(holidayMapper::holidayToDto).collect(Collectors.toList());
        Page<HolidayDto> pageDtos = new PageImpl<>(holidayDtos, PageRequest.of(0, 1), 2);
        when(holidayService.findAll(PageRequest.of(0, 1))).thenReturn(page);

        mockMvc.perform(get("/api/holidays")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(holidayService);
    }

    @Test
    public void whenGetOneHoliday_thenOneHolidayReturned() throws Exception {
        Holiday holiday = createHolidayNoId();
        holiday.setId(1);
        HolidayDto holidayDto = holidayMapper.holidayToDto(holiday);
        when(holidayService.findById(holiday.getId())).thenReturn(holiday);

        mockMvc.perform(get("/api/holidays/{id}", holiday.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(holidayDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveHoliday_thenHolidaySaved() throws Exception {
        Holiday holiday1 = createHolidayNoId();
        Holiday holiday2 = createHolidayNoId();
        holiday2.setId(7);
        HolidayDto holidayDto = holidayMapper.holidayToDto(holiday1);
        when(holidayService.save(holiday1)).thenReturn(holiday2);

        mockMvc.perform(post("/api/holidays")
                .content(objectMapper.writeValueAsString(holidayDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/holidays/7"))
                .andExpect(status().isCreated());

        verify(holidayService).save(holiday1);
    }

    @Test
    public void whenEditHoliday_thenHolidayFound() throws Exception {
        Holiday holiday = createHolidayNoId();
        holiday.setId(1);
        HolidayDto holidayDto = holidayMapper.holidayToDto(holiday);
        when(holidayService.save(holiday)).thenReturn(holiday);

        mockMvc.perform(patch("/api/holidays/{id}", 1)
                .content(objectMapper.writeValueAsString(holidayDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteHoliday_thenHolidayDeleted() throws Exception {
        Holiday holiday = createHolidayNoId();
        holiday.setId(1);
        HolidayDto holidayDto = holidayMapper.holidayToDto(holiday);
        mockMvc.perform(delete("/api/holidays/{id}", 1)
                .content(objectMapper.writeValueAsString(holidayDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(holidayService).delete(holiday);
    }

    private Holiday createHolidayNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Holiday.builder().name("Test Name").date(LocalDate.of(2021, 1, 1)).cathedra(cathedra).build();
    }
}