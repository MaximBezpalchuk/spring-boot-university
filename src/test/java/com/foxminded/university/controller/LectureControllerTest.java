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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.LectureTimeService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;

@ExtendWith(MockitoExtension.class)
public class LectureControllerTest {

	private MockMvc mockMvc;

	@Mock
	private LectureService lectureService;
	@Mock
	private GroupService groupService;
	@Mock
	private TeacherService teacherService;
	@Mock
	private AudienceService audienceService;
	@Mock
	private SubjectService subjectService;
	@Mock
	private CathedraService cathedraService;
	@Mock
	private LectureTimeService lectureTimeService;
	@InjectMocks
	private LectureController lectureController;

	@BeforeEach
	public void setUp() {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setFallbackPageable(PageRequest.of(0, 1));
		mockMvc = MockMvcBuilders.standaloneSetup(lectureController).setCustomArgumentResolvers(resolver).build();
	}

	@Test
	public void whenGetAllLectures_thenAllLecturesReturned() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience audience = Audience.builder().id(1).room(1).capacity(10).build();
		Group group = Group.builder().id(1).name("Group Name").cathedra(cathedra).build();
		Subject subject = Subject.builder()
				.id(1)
				.name("Subject name")
				.description("Subject desc")
				.cathedra(cathedra)
				.build();
		Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra)
				.subjects(Arrays.asList(subject)).build();
		LectureTime time = LectureTime.builder().id(1).start(LocalTime.of(8, 0)).end(LocalTime.of(9, 45)).build();

		Lecture lecture1 = Lecture.builder()
				.id(1)
				.audience(audience)
				.cathedra(cathedra)
				.date(LocalDate.of(2021, 1, 1))
				.group(Arrays.asList(group))
				.subject(subject)
				.teacher(teacher)
				.time(time)
				.build();
		Lecture lecture2 = Lecture.builder()
				.id(2)
				.audience(audience)
				.cathedra(cathedra)
				.date(LocalDate.of(2021, 1, 2))
				.group(Arrays.asList(group))
				.subject(subject)
				.teacher(teacher)
				.time(time)
				.build();
		List<Lecture> lectures = Arrays.asList(lecture1, lecture2);
		Page<Lecture> page = new PageImpl<>(lectures, PageRequest.of(0, 1), 2);
		when(lectureService.findAll(isA(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/lectures"))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/index"))
				.andExpect(forwardedUrl("lectures/index"))
				.andExpect(model().attribute("lectures", page));
		verifyNoMoreInteractions(lectureService);
	}

	@Test
	public void whenGetOneLecture_thenOneLectureReturned() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience audience = Audience.builder().id(1).room(1).capacity(10).build();
		Group group = Group.builder().id(1).name("Group Name").cathedra(cathedra).build();
		Subject subject = Subject.builder()
				.id(1)
				.name("Subject name")
				.description("Subject desc")
				.cathedra(cathedra)
				.build();
		Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra)
				.subjects(Arrays.asList(subject)).build();
		LectureTime time = LectureTime.builder().id(1).start(LocalTime.of(8, 0)).end(LocalTime.of(9, 45)).build();
		Lecture lecture = Lecture.builder()
				.id(1)
				.audience(audience)
				.cathedra(cathedra)
				.date(LocalDate.of(2021, 1, 1))
				.group(Arrays.asList(group))
				.subject(subject)
				.teacher(teacher)
				.time(time)
				.build();
		when(lectureService.findById(lecture.getId())).thenReturn(lecture);

		mockMvc.perform(get("/lectures/{id}", lecture.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/show"))
				.andExpect(forwardedUrl("lectures/show"))
				.andExpect(model().attribute("lecture", lecture));
	}

	@Test
	void whenCreateNewLecture_thenNewLectureCreated() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience audience = Audience.builder().id(1).room(1).capacity(10).build();
		Group group = Group.builder().id(1).name("Group Name").cathedra(cathedra).build();
		Subject subject = Subject.builder().id(1).name("Subject name").description("Subject desc").cathedra(cathedra)
				.build();
		Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra)
				.subjects(Arrays.asList(subject)).build();
		LectureTime time = LectureTime.builder().id(1).start(LocalTime.of(8, 0)).end(LocalTime.of(9, 45)).build();

		when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
		when(audienceService.findAll()).thenReturn(Arrays.asList(audience));
		when(groupService.findAll()).thenReturn(Arrays.asList(group));
		when(subjectService.findAll()).thenReturn(Arrays.asList(subject));
		when(teacherService.findAll()).thenReturn(Arrays.asList(teacher));
		when(lectureTimeService.findAll()).thenReturn(Arrays.asList(time));

		mockMvc.perform(get("/lectures/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/new"))
				.andExpect(forwardedUrl("lectures/new"))
				.andExpect(model().attribute("lecture", instanceOf(Lecture.class)));
	}

	@Test
	void whenSaveLecture_thenLectureSaved() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience audience = Audience.builder().id(1).room(1).capacity(10).build();
		Group group = Group.builder().id(1).name("Group Name").cathedra(cathedra).build();
		Subject subject = Subject.builder().id(1).name("Subject name").description("Subject desc").cathedra(cathedra)
				.build();
		Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra)
				.subjects(Arrays.asList(subject)).build();
		LectureTime time = LectureTime.builder().id(1).start(LocalTime.of(8, 0)).end(LocalTime.of(9, 45)).build();

		Lecture lecture = Lecture.builder()
				.audience(audience)
				.cathedra(cathedra)
				.date(LocalDate.of(2021, 1, 1))
				.group(Arrays.asList(group))
				.subject(subject)
				.teacher(teacher)
				.time(time)
				.build();

		mockMvc.perform(post("/lectures").flashAttr("lecture", lecture))
				.andExpect(redirectedUrl("/lectures"));

		verify(lectureService).save(lecture);
	}

	@Test
	void whenEditLecture_thenLectureFound() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience audience = Audience.builder().id(1).room(1).capacity(10).build();
		Group group = Group.builder().id(1).name("Group Name").cathedra(cathedra).build();
		Subject subject = Subject.builder().id(1).name("Subject name").description("Subject desc").cathedra(cathedra)
				.build();
		Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra)
				.subjects(Arrays.asList(subject)).build();
		LectureTime time = LectureTime.builder().id(1).start(LocalTime.of(8, 0)).end(LocalTime.of(9, 45)).build();

		Lecture expected = Lecture.builder()
				.id(1)
				.audience(audience)
				.cathedra(cathedra)
				.date(LocalDate.of(2021, 1, 1))
				.group(Arrays.asList(group))
				.subject(subject)
				.teacher(teacher)
				.time(time)
				.build();

		when(lectureService.findById(1)).thenReturn(expected);
		when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
		when(audienceService.findAll()).thenReturn(Arrays.asList(audience));
		when(groupService.findAll()).thenReturn(Arrays.asList(group));
		when(subjectService.findAll()).thenReturn(Arrays.asList(subject));
		when(teacherService.findAll()).thenReturn(Arrays.asList(teacher));
		when(lectureTimeService.findAll()).thenReturn(Arrays.asList(time));

		mockMvc.perform(get("/lectures/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/edit"))
				.andExpect(forwardedUrl("lectures/edit"))
				.andExpect(model().attribute("lecture", is(expected)));
	}

	@Test
	void whenDeleteLecture_thenLectureDeleted() throws Exception {
		mockMvc.perform(delete("/lectures/{id}", 1))
				.andExpect(redirectedUrl("/lectures"));

		verify(lectureService).deleteById(1);
	}

	@Test
	void whenShowShedule_thenModelAndViewReturned() throws Exception {
		mockMvc.perform(get("/lectures/shedule"))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/calendar"))
				.andExpect(forwardedUrl("lectures/calendar"));
	}

	@Test
	void whenGetAllLectures_thenStringReturned() throws Exception {
		Subject subject = Subject.builder()
				.id(1)
				.name("Subject name")
				.build();
		LectureTime time = LectureTime.builder().id(1).start(LocalTime.of(8, 0)).end(LocalTime.of(9, 45)).build();
		Lecture lecture1 = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 1, 1))
				.subject(subject)
				.time(time)
				.build();
		Lecture lecture2 = Lecture.builder()
				.id(2)
				.date(LocalDate.of(2021, 1, 2))
				.subject(subject)
				.time(time)
				.build();
		List<Lecture> lectures = Arrays.asList(lecture1, lecture2);
		String expected = "[ {\r\n"
				+ "  \"start\" : \"2021-01-01T08:00:00\",\r\n"
				+ "  \"end\" : \"2021-01-01T09:45:00\",\r\n"
				+ "  \"title\" : \"Subject name\",\r\n"
				+ "  \"url\" : \"/university/lectures/1\"\r\n"
				+ "}, {\r\n"
				+ "  \"start\" : \"2021-01-02T08:00:00\",\r\n"
				+ "  \"end\" : \"2021-01-02T09:45:00\",\r\n"
				+ "  \"title\" : \"Subject name\",\r\n"
				+ "  \"url\" : \"/university/lectures/2\"\r\n"
				+ "} ]";
		when(lectureService.findAll()).thenReturn(lectures);
		MvcResult rt = mockMvc.perform(get("/lectures/shedule/events"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
				.andReturn();
		String actual = rt.getResponse().getContentAsString();

		assertEquals(expected, actual);
	}
}