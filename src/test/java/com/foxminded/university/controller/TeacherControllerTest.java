package com.foxminded.university.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
		Teacher teacher1 = Teacher.builder()
				.id(1)
				.firstName("Name")
				.lastName("Last name")
				.cathedra(cathedra)
				.subjects(Arrays.asList(subject))
				.build();
		Teacher teacher2 = Teacher.builder()
				.id(2)
				.firstName("Name2")
				.lastName("Last name")
				.cathedra(cathedra)
				.subjects(Arrays.asList(subject))
				.build();
		List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
		Page<Teacher> page = new PageImpl<>(teachers, PageRequest.of(0, 2), 1);
		

		when(teacherService.findAll(isA(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/teachers"))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/index"))
				.andExpect(forwardedUrl("teachers/index"))
				.andExpect(model().attribute("teachers", page));
		
		verifyNoMoreInteractions(teacherService);
	}
	
	@Test
    public void whenGetOneTeacher_thenOneTeacherReturned() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Subject subject = Subject.builder().id(1).name("Subject name").build();
		Teacher teacher = Teacher.builder()
				.id(1)
				.firstName("Name")
				.lastName("Last name")
				.cathedra(cathedra)
				.subjects(Arrays.asList(subject))
				.build();
		
		when(teacherService.findById(teacher.getId())).thenReturn(teacher);
		
		 mockMvc.perform(get("/teachers/{id}", teacher.getId()))
		 .andExpect(status().isOk())
         .andExpect(view().name("teachers/show"))
         .andExpect(forwardedUrl("teachers/show"))
         .andExpect(model().attribute("teacher", teacher));
	}
}