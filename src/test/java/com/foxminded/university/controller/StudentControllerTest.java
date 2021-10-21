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

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
	private StudentService studentService;
	@InjectMocks
	private StudentController studentController;
	
	@BeforeEach
    public void setUp() {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setFallbackPageable(PageRequest.of(0, 1));
		mockMvc = MockMvcBuilders.standaloneSetup(studentController).setCustomArgumentResolvers(resolver).build();
    }
	
	@Test
	public void whenGetAllStudents_thenAllStudentsReturned() throws Exception {
		Group group = Group.builder().id(1).name("Killers").build();
		Student student1 = Student.builder()
				.id(1)
				.firstName("Name")
				.lastName("Last name")
				.group(group)
				.build();
		Student student2 = Student.builder()
				.id(2)
				.firstName("Name")
				.lastName("Last name")
				.group(group)
				.build();
		List<Student> students = Arrays.asList(student1, student2);
		Page<Student> page = new PageImpl<>(students, PageRequest.of(0, 1), 2);
		when(studentService.findAll(isA(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/students"))
				.andExpect(status().isOk())
				.andExpect(view().name("students/index"))
				.andExpect(forwardedUrl("students/index"))
				.andExpect(model().attribute("students", page));
		verifyNoMoreInteractions(studentService);
	}
	
	@Test
    public void whenGetOneStudent_thenOneStudentReturned() throws Exception {
		Group group = Group.builder().id(1).name("Killers").build();
		Student student = Student.builder()
				.id(1)
				.firstName("Name")
				.lastName("Last name")
				.group(group)
				.build();
		when(studentService.findById(student.getId())).thenReturn(student);
		
		mockMvc.perform(get("/students/{id}", student.getId()))
		 .andExpect(status().isOk())
         .andExpect(view().name("students/show"))
         .andExpect(forwardedUrl("students/show"))
         .andExpect(model().attribute("student", student));
	}
}