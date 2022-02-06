package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.*;
import com.foxminded.university.service.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LectureRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private LectureService lectureService;
    @InjectMocks
    private LectureRestController lectureRestController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(lectureRestController).setCustomArgumentResolvers(resolver).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
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
        Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra).gender(Gender.MALE)
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
        when(lectureService.findAll(PageRequest.of(0, 1))).thenReturn(page);

        mockMvc.perform(get("/api/lectures"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(1)))
            .andExpect(jsonPath("$.content[0].cathedra.id", is(1)))
            .andExpect(jsonPath("$.content[0].cathedra.name", is("Fantastic Cathedra")))
            .andExpect(jsonPath("$.content[0].groups[0].id", is(1)))
            .andExpect(jsonPath("$.content[0].groups[0].name", is("Group Name")))
            .andExpect(jsonPath("$.content[0].teacher.id", is(1)))
            .andExpect(jsonPath("$.content[0].teacher.firstName", is("Test Name")))
            .andExpect(jsonPath("$.content[0].teacher.lastName", is("Last Name")))
            .andExpect(jsonPath("$.content[0].audience.id", is(1)))
            .andExpect(jsonPath("$.content[0].audience.room", is(1)))
            .andExpect(jsonPath("$.content[0].date", is("2021-01-01")))
            .andExpect(jsonPath("$.content[0].subject.id", is(1)))
            .andExpect(jsonPath("$.content[0].subject.name", is("Subject name")))
            .andExpect(jsonPath("$.content[0].time.id", is(1)))
            .andExpect(jsonPath("$.content[1].id", is(2)))
            .andExpect(jsonPath("$.content[1].cathedra.id", is(1)))
            .andExpect(jsonPath("$.content[1].cathedra.name", is("Fantastic Cathedra")))
            .andExpect(jsonPath("$.content[1].groups[0].id", is(1)))
            .andExpect(jsonPath("$.content[1].groups[0].name", is("Group Name")))
            .andExpect(jsonPath("$.content[1].teacher.id", is(1)))
            .andExpect(jsonPath("$.content[1].teacher.firstName", is("Test Name")))
            .andExpect(jsonPath("$.content[1].teacher.lastName", is("Last Name")))
            .andExpect(jsonPath("$.content[1].audience.id", is(1)))
            .andExpect(jsonPath("$.content[1].audience.room", is(1)))
            .andExpect(jsonPath("$.content[1].date", is("2021-01-02")))
            .andExpect(jsonPath("$.content[1].subject.id", is(1)))
            .andExpect(jsonPath("$.content[1].subject.name", is("Subject name")))
            .andExpect(jsonPath("$.content[1].time.id", is(1)))
            .andExpect(jsonPath("$.totalElements", is(2)))
            .andExpect(jsonPath("$.pageable.paged", is(true)));
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
        Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra).gender(Gender.MALE)
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

        mockMvc.perform(get("/api/lectures/{id}", lecture.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.cathedra.id", is(1)))
            .andExpect(jsonPath("$.cathedra.name", is("Fantastic Cathedra")))
            .andExpect(jsonPath("$.groups[0].id", is(1)))
            .andExpect(jsonPath("$.groups[0].name", is("Group Name")))
            .andExpect(jsonPath("$.teacher.id", is(1)))
            .andExpect(jsonPath("$.teacher.firstName", is("Test Name")))
            .andExpect(jsonPath("$.teacher.lastName", is("Last Name")))
            .andExpect(jsonPath("$.audience.id", is(1)))
            .andExpect(jsonPath("$.audience.room", is(1)))
            .andExpect(jsonPath("$.date", is("2021-01-01")))
            .andExpect(jsonPath("$.subject.id", is(1)))
            .andExpect(jsonPath("$.subject.name", is("Subject name")))
            .andExpect(jsonPath("$.time.id", is(1)));
    }

    @Test
    void whenSaveLecture_thenLectureSaved() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience = Audience.builder().id(1).room(1).capacity(10).build();
        Group group = Group.builder().id(1).name("Group Name").cathedra(cathedra).build();
        Subject subject = Subject.builder().id(1).name("Subject name").description("Subject desc").cathedra(cathedra)
            .build();
        Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra).gender(Gender.MALE)
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

        mockMvc.perform(post("/api/lectures")
            .content(objectMapper.writeValueAsString(lecture))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(lectureService).save(lecture);
    }

    @Test
    void whenEditLecture_thenLectureFound() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience = Audience.builder().id(1).room(1).capacity(10).build();
        Group group = Group.builder().id(1).name("Group Name").cathedra(cathedra).build();
        Subject subject = Subject.builder().id(1).name("Subject name").description("Subject desc").cathedra(cathedra)
            .build();
        Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra).gender(Gender.MALE)
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

        mockMvc.perform(patch("/api/lectures/{id}", 1)
            .content(objectMapper.writeValueAsString(lecture))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void whenDeleteLecture_thenLectureDeleted() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience = Audience.builder().id(1).room(1).capacity(10).build();
        Group group = Group.builder().id(1).name("Group Name").cathedra(cathedra).build();
        Subject subject = Subject.builder().id(1).name("Subject name").description("Subject desc").cathedra(cathedra)
            .build();
        Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra).gender(Gender.MALE)
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

        mockMvc.perform(delete("/api/lectures/{id}", 1)
            .content(objectMapper.writeValueAsString(lecture))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(lectureService).delete(lecture);
    }
}