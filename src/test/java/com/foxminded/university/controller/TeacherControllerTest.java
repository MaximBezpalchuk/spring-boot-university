package com.foxminded.university.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

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
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.TeacherService;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
	private TeacherService teacherService;
	@InjectMocks
	private TeacherController teacherController;
	
	@BeforeEach
	public void setMocks() {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 2));
		mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
				.setCustomArgumentResolvers(resolver).build();
	}
	
	@Test
	public void whenGetAllTeachers_thenAllTeachersReturned() throws Exception {
		
		
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Subject subject = Subject.builder().id(1).name("Subject name").build();
		Teacher first = Teacher.builder()
				.id(1)
				.firstName("Name")
				.lastName("Last name")
				.cathedra(cathedra)
				.subjects(Arrays.asList(subject))
				.build();
		Teacher second = Teacher.builder()
				.id(2)
				.firstName("Name2")
				.lastName("Last name")
				.cathedra(cathedra)
				.subjects(Arrays.asList(subject))
				.build();
		List<Teacher> list = Arrays.asList(first, second);
		Page<Teacher> page = new PageImpl<>(list, PageRequest.of(0, 2), 1);
		

		when(this.teacherService.findAll(isA(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/teachers"))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/index"))
				.andExpect(forwardedUrl("teachers/index"))
				.andExpect(model().attribute("teachers", page));
		verify(teacherService, times(1)).findAll(PageRequest.of(0, 2));
		verifyNoMoreInteractions(teacherService);
	}
}