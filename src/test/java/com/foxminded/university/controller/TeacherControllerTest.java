package com.foxminded.university.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.dao.jdbc.mapper.LectureToEventMapper;
import com.foxminded.university.model.*;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

	private MockMvc mockMvc;
	private LectureToEventMapper lectureToEventMapper = LectureToEventMapper.INSTANCE;
	@Mock
	private TeacherService teacherService;
	@Mock
	private SubjectService subjectService;
	@Mock
	private CathedraService cathedraService;
	@Mock
	private LectureService lectureService;
	@InjectMocks
	private TeacherController teacherController;


	@BeforeEach
	public void setUp() {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setFallbackPageable(PageRequest.of(0, 2));
		mockMvc = MockMvcBuilders.standaloneSetup(teacherController).setCustomArgumentResolvers(resolver).build();
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

	@Test
	void whenShowShedule_thenModelAndViewReturned() throws Exception {
		mockMvc.perform(get("/teachers/1/shedule"))
				.andExpect(status().isOk())
				.andExpect(view().name("teachers/calendar"))
				.andExpect(forwardedUrl("teachers/calendar"));
	}

	@Test
	void whenGetLecturesByTeacherId_thenStringReturned() throws Exception {
		Subject subject = Subject.builder()
				.id(1)
				.name("Subject name")
				.build();
		LectureTime time = LectureTime.builder().id(1).start(LocalTime.of(8, 0)).end(LocalTime.of(9, 45)).build();
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 1, 1))
				.subject(subject)
				.time(time)
				.build();
		ReflectionTestUtils.setField(teacherController, "lectureToEventMapper", lectureToEventMapper);
		when(lectureService.findByTeacherId(1)).thenReturn(Arrays.asList(lecture));
		String expected = "[ {\r\n"
				+ "  \"title\" : \"Subject name\",\r\n"
				+ "  \"start\" : \"2021-01-01T08:00:00\",\r\n"
				+ "  \"end\" : \"2021-01-01T09:45:00\",\r\n"
				+ "  \"url\" : \"/university/lectures/1\"\r\n"
				+ "} ]";
		MvcResult rt = mockMvc.perform(get("/teachers/1/shedule/events"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn();
		String actual = rt.getResponse().getContentAsString();

		assertEquals(expected, actual);
	}
}