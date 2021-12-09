package com.foxminded.university.controller;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class HolidayControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HolidayService holidayService;
    @Mock
    private CathedraService cathedraService;
    @InjectMocks
    private HolidayController holidayController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController).setCustomArgumentResolvers(resolver).build();
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
        when(holidayService.findAll(isA(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/holidays"))
            .andExpect(status().isOk())
            .andExpect(view().name("holidays/index"))
            .andExpect(forwardedUrl("holidays/index"))
            .andExpect(model().attribute("holidays", page));
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

        mockMvc.perform(get("/holidays/{id}", holiday.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("holidays/show"))
            .andExpect(forwardedUrl("holidays/show"))
            .andExpect(model().attribute("holiday", holiday));
    }

    @Test
    void whenCreateNewHoliday_thenNewHolidayCreated() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));

        mockMvc.perform(get("/holidays/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("holidays/new"))
            .andExpect(forwardedUrl("holidays/new"))
            .andExpect(model().attribute("holiday", instanceOf(Holiday.class)));
    }

    @Test
    void whenSaveHoliday_thenHolidaySaved() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Holiday holiday = Holiday.builder()
            .name("Test Name2")
            .date(LocalDate.of(2021, 1, 1))
            .cathedra(cathedra)
            .build();

        mockMvc.perform(post("/holidays").flashAttr("holiday", holiday))
            .andExpect(redirectedUrl("/holidays"));

        verify(holidayService).save(holiday);
    }

    @Test
    void whenEditHoliday_thenHolidayFound() throws Exception {
        Cathedra cathedra = Cathedra.builder()
            .id(1)
            .name("Fantastic Cathedra")
            .build();
        Holiday expected = Holiday.builder()
            .id(1)
            .name("Test Name")
            .date(LocalDate.of(2021, 1, 1))
            .cathedra(cathedra)
            .build();

        when(holidayService.findById(1)).thenReturn(expected);
        when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));

        mockMvc.perform(get("/holidays/{id}/edit", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("holidays/edit"))
            .andExpect(forwardedUrl("holidays/edit"))
            .andExpect(model().attribute("holiday", is(expected)));
    }

    @Test
    void whenDeleteHoliday_thenHolidayDeleted() throws Exception {
        mockMvc.perform(delete("/holidays/{id}", 1))
            .andExpect(redirectedUrl("/holidays"));

        verify(holidayService).delete(Holiday.builder().id(1).build());
    }
}