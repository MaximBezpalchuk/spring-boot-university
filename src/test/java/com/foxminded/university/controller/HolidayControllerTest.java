package com.foxminded.university.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.HolidayService;

@ExtendWith(MockitoExtension.class)
public class HolidayControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
	private HolidayService holidayService;
	@InjectMocks
	private HolidayController holidayController;
	
	@BeforeEach
    public void setMocks() {
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

		when(this.holidayService.findAll(isA(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/holidays"))
				.andExpect(status().isOk())
				.andExpect(view().name("holidays/index"))
				.andExpect(forwardedUrl("holidays/index"))
				.andExpect(model().attribute("holidays", page));
		
		verifyNoMoreInteractions(holidayService);
	}
}