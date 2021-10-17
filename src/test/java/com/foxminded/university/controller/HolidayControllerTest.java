package com.foxminded.university.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.HolidayService;

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
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController).build();
    }
	
	@Test
	public void whenGetAllHolidays_thenAllHolidaysReturned() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Holiday first = Holiday.builder()
				.id(1)
				.name("Test Name")
				.date(LocalDate.of(2021, 1, 1))
				.cathedra(cathedra)
				.build();
		Holiday second = Holiday.builder()
				.id(2)
				.name("Test Name2")
				.date(LocalDate.of(2021, 1, 2))
				.cathedra(cathedra)
				.build();
		List<Holiday> list = Arrays.asList(first, second);
		Page<Holiday> holidayPage = new PageImpl<>(list, PageRequest.of(0, 1), 2);

		when(holidayService.findPaginatedHolidays(PageRequest.of(0, 1))).thenReturn(holidayPage);

		mockMvc.perform(get("/holidays?size=1"))
				.andExpect(status().isOk())
				.andExpect(view().name("holidays/index"))
				.andExpect(forwardedUrl("holidays/index"))
				.andExpect(model().attribute("holidayPage", holidayPage))
				.andExpect(model().attribute("pageNumbers", Arrays.asList(1, 2)));

		verify(holidayService, times(1)).findPaginatedHolidays(PageRequest.of(0, 1));
		verifyNoMoreInteractions(holidayService);
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
		
		verify(holidayService).deleteById(1);
	}
}