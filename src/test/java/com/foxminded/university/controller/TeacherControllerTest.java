package com.foxminded.university.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
	private TeacherService teacherService;
	@Mock
	private SubjectService subjectService;
	@Mock
	private CathedraService cathedraService;
	@InjectMocks
	private TeacherController teacherController;
	
	@BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
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
		Page<Teacher> teacherPage = new PageImpl<>(list, PageRequest.of(0, 1), 2);

		when(teacherService.findPaginatedTeachers(PageRequest.of(0, 1))).thenReturn(teacherPage);

		mockMvc.perform(get("/teachers?size=1"))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/index"))
				.andExpect(forwardedUrl("teachers/index"))
				.andExpect(model().attribute("teacherPage", teacherPage))
				.andExpect(model().attribute("pageNumbers", Arrays.asList(1, 2)));

		verify(teacherService, times(1)).findPaginatedTeachers(PageRequest.of(0, 1));
		verifyNoMoreInteractions(teacherService);
	}
	
	@Test
	void whenCreateNewTeacher_thenNewTeacherCreated() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Subject subject = Subject.builder().id(1).name("Subject name").build();
		
		when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
		when(subjectService.findAll()).thenReturn(Arrays.asList(subject));
		
		mockMvc.perform(get("/teachers/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/new"))
				.andExpect(forwardedUrl("teachers/new"))
				.andExpect(model().attribute("teacher", instanceOf(Teacher.class)));
	}
	
	@Test
	void whenSaveTeacher_thenTeacherSaved() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Subject subject = Subject.builder().id(1).name("Subject name").build();
		Teacher teacher = Teacher.builder()
				.firstName("Name")
				.lastName("Last name")
				.cathedra(cathedra)
				.subjects(Arrays.asList(subject))
				.build();
		
		mockMvc.perform(post("/teachers").flashAttr("teacher", teacher))		
				.andExpect(redirectedUrl("/teachers"));
		
		verify(teacherService).save(teacher);
	}

	@Test
	void whenEditTeacher_thenTeacherFound() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Subject subject = Subject.builder().id(1).name("Subject name").build();
		Teacher expected = Teacher.builder()
				.id(1)
				.firstName("Name")
				.lastName("Last name")
				.cathedra(cathedra)
				.subjects(Arrays.asList(subject))
				.build();
		
		when(teacherService.findById(1)).thenReturn(expected);
		when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
		when(subjectService.findAll()).thenReturn(Arrays.asList(subject));
		
		mockMvc.perform(get("/teachers/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/edit"))
				.andExpect(forwardedUrl("teachers/edit"))
				.andExpect(model().attribute("teacher", is(expected)));
	}
	
	@Test
	void whenDeleteTeacher_thenTeacherDeleted() throws Exception {
		mockMvc.perform(delete("/teachers/{id}", 1))
				.andExpect(redirectedUrl("/teachers"));
		
		verify(teacherService).deleteById(1);
	}
}