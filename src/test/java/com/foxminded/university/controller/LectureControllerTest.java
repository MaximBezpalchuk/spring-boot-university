package com.foxminded.university.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.LectureService;

@ExtendWith(MockitoExtension.class)
public class LectureControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
	private LectureService lectureService;
	@InjectMocks
	private LectureController lectureController;
	
	@BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(lectureController).build();
    }
	
	@Test
	public void whenGetAllLectures_thenAllLecturesReturned() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience audience = Audience.builder().id(1).room(1).capacity(10).build();
		Group group = Group.builder().id(1).name("Group Name").cathedra(cathedra).build();
		Subject subject = Subject.builder().id(1).name("Subject name").description("Subject desc").cathedra(cathedra).build();
		Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra).subjects(Arrays.asList(subject)).build();
		LectureTime time = LectureTime.builder().id(1).start(LocalTime.of(8, 0)).end(LocalTime.of(9, 45)).build();
		
		Lecture first = Lecture.builder()
				.id(1)
				.audience(audience)
				.cathedra(cathedra)
				.date(LocalDate.of(2021, 1, 1))
				.group(Arrays.asList(group))
				.subject(subject)
				.teacher(teacher)
				.time(time)
				.build();
		Lecture second = Lecture.builder()
				.id(2)
				.audience(audience)
				.cathedra(cathedra)
				.date(LocalDate.of(2021, 1, 2))
				.group(Arrays.asList(group))
				.subject(subject)
				.teacher(teacher)
				.time(time)
				.build();
		List<Lecture> list = Arrays.asList(first, second);
		Page<Lecture> lecturePage = new PageImpl<>(list, PageRequest.of(0, 1), 2);

		when(lectureService.findAll(PageRequest.of(0, 1))).thenReturn(lecturePage);

		mockMvc.perform(get("/lectures"))
				.andExpect(status().isOk())
				.andExpect(view().name("lectures/index"))
				.andExpect(forwardedUrl("lectures/index"))
				.andExpect(model().attribute("lecturePage", lecturePage))
				.andExpect(model().attribute("pageNumbers", Arrays.asList(1, 2)));

		verify(lectureService, times(1)).findAll(PageRequest.of(0, 1));
		verifyNoMoreInteractions(lectureService);
	}
}