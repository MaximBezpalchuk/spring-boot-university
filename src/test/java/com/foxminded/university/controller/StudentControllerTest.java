package com.foxminded.university.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import com.foxminded.university.dao.mapper.LectureToEventMapper;
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

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

	private MockMvc mockMvc;
	private LectureToEventMapper lectureToEventMapper = LectureToEventMapper.INSTANCE;
	@Mock
	private StudentService studentService;
	@Mock
	private GroupService groupService;
	@Mock
	private LectureService lectureService;
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

	@Test
	void whenCreateNewStudent_thenNewStudentCreated() throws Exception {
		Group group = Group.builder().id(1).name("Killers").build();

		when(groupService.findAll()).thenReturn(Arrays.asList(group));

		mockMvc.perform(get("/students/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("students/new"))
				.andExpect(forwardedUrl("students/new"))
				.andExpect(model().attribute("student", instanceOf(Student.class)));
	}

	@Test
	void whenSaveStudent_thenStudentSaved() throws Exception {
		Group group = Group.builder().id(1).name("Killers").build();
		Student student = Student.builder()
				.firstName("Name")
				.lastName("Last name")
				.group(group)
				.build();
		mockMvc.perform(post("/students").flashAttr("student", student))
				.andExpect(redirectedUrl("/students"));

		verify(studentService).save(student);
	}

	@Test
	void whenEditStudent_thenStudentFound() throws Exception {
		Group group = Group.builder().id(1).name("Killers").build();
		Student expected = Student.builder()
				.id(1)
				.firstName("Name")
				.lastName("Last name")
				.group(group)
				.build();

		when(studentService.findById(1)).thenReturn(expected);
		when(groupService.findAll()).thenReturn(Arrays.asList(group));

		mockMvc.perform(get("/students/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("students/edit"))
				.andExpect(forwardedUrl("students/edit"))
				.andExpect(model().attribute("student", is(expected)));
	}

	@Test
	void whenDeleteStudent_thenStudentDeleted() throws Exception {
		mockMvc.perform(delete("/students/{id}", 1))
				.andExpect(redirectedUrl("/students"));

		verify(studentService).delete(Student.builder().id(1).build());
	}
}