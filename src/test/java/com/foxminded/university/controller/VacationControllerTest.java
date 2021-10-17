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

import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;

@ExtendWith(MockitoExtension.class)
public class VacationControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
	private TeacherService teacherService;
	@Mock
	private VacationService vacationService;
	@InjectMocks
	private VacationController vacationController;
	
	@BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(vacationController).build();
    }
	
	@Test
	public void whenGetAllVacations_thenAllVacationsReturned() throws Exception {
		Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).build();
		Vacation first = Vacation.builder()
				.id(1)
				.teacher(teacher)
				.start(LocalDate.of(2021, 1, 1))
				.end(LocalDate.of(2021, 1, 2))
				.build();
		Vacation second = Vacation.builder()
				.id(1)
				.teacher(teacher)
				.start(LocalDate.of(2020, 1, 1))
				.end(LocalDate.of(2020, 1, 2))
				.build();
		List<Vacation> list = Arrays.asList(first, second);
		Page<Vacation> vacationPage = new PageImpl<>(list, PageRequest.of(0, 1), 2);

		when(vacationService.findPaginatedVacationsByTeacherId(PageRequest.of(0, 1), teacher.getId())).thenReturn(vacationPage);

		mockMvc.perform(get("/teachers/{id}/vacations?size=1", teacher.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/vacations/index"))
				.andExpect(forwardedUrl("teachers/vacations/index"))
				.andExpect(model().attribute("vacationPage", vacationPage))
				.andExpect(model().attribute("pageNumbers", Arrays.asList(1, 2)));

		verify(vacationService, times(1)).findPaginatedVacationsByTeacherId(PageRequest.of(0, 1), teacher.getId());
		verifyNoMoreInteractions(vacationService);
	}
	
	@Test
	void whenCreateNewVacation_thenNewVacationCreated() throws Exception {
		Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).build();
		
		when(teacherService.findById(teacher.getId())).thenReturn(teacher);
		
		mockMvc.perform(get("/teachers/{id}/vacations/new", teacher.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/vacations/new"))
				.andExpect(forwardedUrl("teachers/vacations/new"))
				.andExpect(model().attribute("vacation", instanceOf(Vacation.class)));
	}
	
	@Test
	void whenSaveVacation_thenVacationSaved() throws Exception {
		Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).build();
		Vacation vacation = Vacation.builder()
				.teacher(teacher)
				.start(LocalDate.of(2021, 1, 1))
				.end(LocalDate.of(2021, 1, 2))
				.build();
		mockMvc.perform(post("/teachers/{id}/vacations", teacher.getId()).flashAttr("vacation", vacation))		
				.andExpect(redirectedUrl("/teachers/"+ teacher.getId() +"/vacations"));
		
		verify(vacationService).save(vacation);
	}

	@Test
	void whenEditVacation_thenVacationFound() throws Exception {
		Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).build();
		Vacation expected = Vacation.builder()
				.id(1)
				.teacher(teacher)
				.start(LocalDate.of(2021, 1, 1))
				.end(LocalDate.of(2021, 1, 2))
				.build();
		
		when(vacationService.findById(1)).thenReturn(expected);
		when(teacherService.findById(teacher.getId())).thenReturn(teacher);
		
		mockMvc.perform(get("/teachers/{id}/vacations/{vacId}/edit", teacher.getId(), 1))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/vacations/edit"))
				.andExpect(forwardedUrl("teachers/vacations/edit"))
				.andExpect(model().attribute("vacation", is(expected)));
	}
	
	@Test
	void whenDeleteVacation_thenVacationDeleted() throws Exception {
		mockMvc.perform(delete("/teachers/{id}/vacations/{vacId}", 1, 1))
				.andExpect(redirectedUrl("/teachers/1/vacations"));
		
		verify(vacationService).deleteById(1);
	}
}