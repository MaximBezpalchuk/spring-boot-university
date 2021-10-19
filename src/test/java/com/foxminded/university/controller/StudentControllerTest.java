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
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }
	
	@Test
	public void whenGetAllStudents_thenAllStudentsReturned() throws Exception {
		Group group = Group.builder().id(1).name("Killers").build();
		Student first = Student.builder()
				.id(1)
				.firstName("Name")
				.lastName("Last name")
				.group(group)
				.build();
		Student second = Student.builder()
				.id(2)
				.firstName("Name")
				.lastName("Last name")
				.group(group)
				.build();
		List<Student> list = Arrays.asList(first, second);
		Page<Student> studentPage = new PageImpl<>(list, PageRequest.of(0, 1), 2);

		when(studentService.findAll(PageRequest.of(0, 1))).thenReturn(studentPage);

		mockMvc.perform(get("/students"))
				.andExpect(status().isOk())
				.andExpect(view().name("students/index"))
				.andExpect(forwardedUrl("students/index"))
				.andExpect(model().attribute("studentPage", studentPage))
				.andExpect(model().attribute("pageNumbers", Arrays.asList(1, 2)));

		verify(studentService, times(1)).findAll(PageRequest.of(0, 1));
		verifyNoMoreInteractions(studentService);
	}
}