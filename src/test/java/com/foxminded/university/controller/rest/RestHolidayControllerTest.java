package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.HolidayService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestHolidayControllerTest {


    private MockMvc mockMvc;

    @Mock
    private HolidayService holidayService;
    @InjectMocks
    private RestHolidayController restHolidayController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(restHolidayController).setCustomArgumentResolvers(resolver).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void whenGetAllHolidays_thenAllHolidaysReturned() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Holiday holiday1 = Holiday.builder()
            .id(1)
            .name("Test Name")
            .date(LocalDate.of(2021, 1, 1))
            .cathedra(cathedra)
            .build();
        Holiday holiday2 = Holiday.builder()
            .id(2)
            .name("Test Name2")
            .date(LocalDate.of(2021, 1, 2))
            .cathedra(cathedra)
            .build();
        List<Holiday> holidays = Arrays.asList(holiday1, holiday2);
        Page<Holiday> page = new PageImpl<>(holidays, PageRequest.of(0, 1), 2);
        when(holidayService.findAll(PageRequest.of(0, 1))).thenReturn(page);

        mockMvc.perform(get("/api/holidays"))
            .andExpect(jsonPath("$.content[0].id", is(1)))
            .andExpect(jsonPath("$.content[0].name", is("Test Name")))
            .andExpect(jsonPath("$.content[0].date", is("2021-01-01")))
            .andExpect(jsonPath("$.content[0].cathedra.id", is(1)))
            .andExpect(jsonPath("$.content[0].cathedra.name", is("Fantastic Cathedra")))
            .andExpect(jsonPath("$.content[1].id", is(2)))
            .andExpect(jsonPath("$.content[1].name", is("Test Name2")))
            .andExpect(jsonPath("$.content[1].date", is("2021-01-02")))
            .andExpect(jsonPath("$.content[1].cathedra.id", is(1)))
            .andExpect(jsonPath("$.content[1].cathedra.name", is("Fantastic Cathedra")))
            .andExpect(jsonPath("$.totalElements", is(2)))
            .andExpect(jsonPath("$.pageable.paged", is(true)));
        verifyNoMoreInteractions(holidayService);
    }

    @Test
    public void whenGetOneHoliday_thenOneHolidayReturned() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Holiday holiday = Holiday.builder()
            .id(1)
            .name("Test Name")
            .date(LocalDate.of(2021, 1, 1))
            .cathedra(cathedra)
            .build();
        when(holidayService.findById(holiday.getId())).thenReturn(holiday);

        mockMvc.perform(get("/api/holidays/{id}", holiday.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Matchers.is(1)))
            .andExpect(jsonPath("$.name", Matchers.is("Test Name")))
            .andExpect(jsonPath("$.date", Matchers.is("2021-01-01")))
            .andExpect(jsonPath("$.cathedra.id", Matchers.is(1)))
            .andExpect(jsonPath("$.cathedra.name", Matchers.is("Fantastic Cathedra")));
    }

    @Test
    void whenSaveHoliday_thenHolidaySaved() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Holiday holiday = Holiday.builder()
            .name("Test Name2")
            .date(LocalDate.of(2021, 1, 1))
            .cathedra(cathedra)
            .build();

        mockMvc.perform(post("/api/holidays")
            .content(objectMapper.writeValueAsString(holiday))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(holidayService).save(holiday);
    }

    @Test
    void whenEditHoliday_thenHolidayFound() throws Exception {
        Cathedra cathedra = Cathedra.builder()
            .id(1)
            .name("Fantastic Cathedra")
            .build();
        Holiday holiday = Holiday.builder()
            .id(1)
            .name("Test Name")
            .date(LocalDate.of(2021, 1, 1))
            .cathedra(cathedra)
            .build();

        mockMvc.perform(patch("/api/holidays/{id}", 1)
            .content(objectMapper.writeValueAsString(holiday))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void whenDeleteHoliday_thenHolidayDeleted() throws Exception {
        Cathedra cathedra = Cathedra.builder()
            .id(1)
            .name("Fantastic Cathedra")
            .build();
        Holiday holiday = Holiday.builder()
            .id(1)
            .name("Test Name")
            .date(LocalDate.of(2021, 1, 1))
            .cathedra(cathedra)
            .build();
        mockMvc.perform(delete("/api/holidays/{id}", 1)
            .content(objectMapper.writeValueAsString(holiday))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(holidayService).delete(holiday);
    }
}