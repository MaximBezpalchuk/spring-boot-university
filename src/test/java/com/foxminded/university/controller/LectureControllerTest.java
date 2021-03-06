package com.foxminded.university.controller;

import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dao.mapper.LectureToEventMapper;
import com.foxminded.university.model.*;
import com.foxminded.university.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LectureControllerTest {

    private MockMvc mockMvc;
    private final LectureToEventMapper lectureToEventMapper = Mappers.getMapper(LectureToEventMapper.class);

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
    private StudentService studentService;
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
            .groups(Arrays.asList(group))
            .subject(subject)
            .teacher(teacher)
            .time(time)
            .build();
        Lecture lecture2 = Lecture.builder()
            .id(2)
            .audience(audience)
            .cathedra(cathedra)
            .date(LocalDate.of(2021, 1, 2))
            .groups(Arrays.asList(group))
            .subject(subject)
            .teacher(teacher)
            .time(time)
            .build();
        List<Lecture> lectures = Arrays.asList(lecture1, lecture2);
        Page<Lecture> page = new PageImpl<>(lectures, PageRequest.of(0, 1), 2);
        when(lectureService.findAll(PageRequest.of(0, 1))).thenReturn(page);

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
            .groups(Arrays.asList(group))
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
            .groups(Arrays.asList(group))
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
            .groups(Arrays.asList(group))
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

        verify(lectureService).delete(1);
    }

    @Test
    void whenShowStudentsShedule_thenModelAndViewReturned() throws Exception {
        when(studentService.findById(1)).thenReturn(Student.builder().id(1).build());
        mockMvc.perform(get("/students/1/shedule"))
            .andExpect(status().isOk())
            .andExpect(view().name("students/calendar_chose_period"))
            .andExpect(forwardedUrl("students/calendar_chose_period"));
    }

    @Test
    void whenPostShowStudentsShedule_thenModelAndViewReturned() throws Exception {
        when(studentService.findById(1)).thenReturn(Student.builder().id(1).build());
        String body = "[ {\r\n"
            + "  \"start\" : \"2021-04-04\",\r\n"
            + "  \"end\" : \"2021-04-04\",\r\n"
            + "} ]";
        mockMvc.perform(post("/students/1/shedule").content(body))
            .andExpect(status().isOk())
            .andExpect(view().name("students/calendar"))
            .andExpect(forwardedUrl("students/calendar"));
    }

    @Test
    void whenGetLecturesByStudentId_thenStringReturned() throws Exception {
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
        ReflectionTestUtils.setField(lectureController, "lectureToEventMapper", lectureToEventMapper);
        when(lectureService.findByStudentIdAndPeriod(1, LocalDate.of(2021, 4, 4), LocalDate.of(2021, 4, 8)))
            .thenReturn(Arrays.asList(lecture));
        String expected = "[ {\r\n"
            + "  \"title\" : \"Subject name\",\r\n"
            + "  \"start\" : \"2021-01-01T08:00:00\",\r\n"
            + "  \"end\" : \"2021-01-01T09:45:00\",\r\n"
            + "  \"url\" : \"/lectures/1\"\r\n"
            + "} ]";
        MvcResult rt = mockMvc.perform(get("/students/1/shedule/events?start=2021-04-04&end=2021-04-08"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andReturn();
        String actual = rt.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void whenShowShedule_thenModelAndViewReturned() throws Exception {
        when(teacherService.findById(1)).thenReturn(Teacher.builder().id(1).build());
        mockMvc.perform(get("/teachers/1/shedule"))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/calendar_chose_period"))
            .andExpect(forwardedUrl("teachers/calendar_chose_period"));
    }

    @Test
    void whenPostShowTeachersShedule_thenModelAndViewReturned() throws Exception {
        when(teacherService.findById(1)).thenReturn(Teacher.builder().id(1).build());
        String body = "[ {\r\n"
            + "  \"start\" : \"2021-04-04\",\r\n"
            + "  \"end\" : \"2021-04-04\",\r\n"
            + "} ]";
        mockMvc.perform(post("/teachers/1/shedule").content(body))
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
        ReflectionTestUtils.setField(lectureController, "lectureToEventMapper", lectureToEventMapper);
        when(lectureService.findByTeacherIdAndPeriod(1, LocalDate.of(2021, 4, 4), LocalDate.of(2021, 4, 8)))
            .thenReturn(Arrays.asList(lecture));
        String expected = "[ {\r\n"
            + "  \"title\" : \"Subject name\",\r\n"
            + "  \"start\" : \"2021-01-01T08:00:00\",\r\n"
            + "  \"end\" : \"2021-01-01T09:45:00\",\r\n"
            + "  \"url\" : \"/lectures/1\"\r\n"
            + "} ]";
        MvcResult rt = mockMvc.perform(get("/teachers/1/shedule/events?start=2021-04-04&end=2021-04-08"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andReturn();
        String actual = rt.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void whenEditTeacher_thenTeachersFound() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience = Audience.builder().id(1).room(1).capacity(10).build();
        Group group = Group.builder().id(1).name("Group Name").cathedra(cathedra).build();
        Subject subject = Subject.builder().id(1).name("Subject name").description("Subject desc").cathedra(cathedra)
            .build();
        Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra)
            .subjects(Arrays.asList(subject)).build();
        LectureTime time = LectureTime.builder().id(1).start(LocalTime.of(8, 0)).end(LocalTime.of(9, 45)).build();

        Lecture lecture = Lecture.builder()
            .id(1)
            .audience(audience)
            .cathedra(cathedra)
            .date(LocalDate.of(2021, 1, 1))
            .groups(Arrays.asList(group))
            .subject(subject)
            .teacher(teacher)
            .time(time)
            .build();

        when(lectureService.findById(1)).thenReturn(lecture);
        when(teacherService.findTeachersForChange(lecture)).thenReturn(Arrays.asList(teacher));

        mockMvc.perform(get("/lectures/{id}/edit/teacher", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("lectures/edit_teacher"))
            .andExpect(forwardedUrl("lectures/edit_teacher"))
            .andExpect(model().attribute("lecture", is(lecture)))
            .andExpect(model().attribute("teachers", is(Arrays.asList(teacher))));
    }
}