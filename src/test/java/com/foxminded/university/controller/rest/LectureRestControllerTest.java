package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.HolidayMapper;
import com.foxminded.university.dao.mapper.LectureMapper;
import com.foxminded.university.dao.mapper.TeacherMapper;
import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.dto.ObjectListDto;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LectureRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private final LectureMapper lectureMapper = Mappers.getMapper(LectureMapper.class);
    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper .class);
    @Mock
    private LectureService lectureService;
    @Mock
    private LectureTimeService lectureTimeService;
    @Mock
    private SubjectService subjectService;
    @Mock
    private AudienceService audienceService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private CathedraService cathedraService;
    @Mock
    private GroupService groupService;
    @InjectMocks
    private LectureRestController lectureRestController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(lectureRestController).setCustomArgumentResolvers(resolver).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ReflectionTestUtils.setField(lectureRestController, "lectureMapper", lectureMapper);
        ReflectionTestUtils.setField(lectureMapper, "cathedraService", cathedraService);
        ReflectionTestUtils.setField(lectureMapper, "lectureTimeService", lectureTimeService);
        ReflectionTestUtils.setField(lectureMapper, "subjectService", subjectService);
        ReflectionTestUtils.setField(lectureMapper, "audienceService", audienceService);
        ReflectionTestUtils.setField(lectureMapper, "teacherService", teacherService);
        ReflectionTestUtils.setField(lectureMapper, "teacherMapper", teacherMapper);
        ReflectionTestUtils.setField(lectureMapper, "groupService", groupService);
    }

    @Test
    public void whenGetAllLectures_thenAllLecturesReturned() throws Exception {
        Lecture lecture1 = createLectureNoId();
        lecture1.setId(1);
        Lecture lecture2 = createLectureNoId();
        lecture2.setId(2);
        List<Lecture> lectures = Arrays.asList(lecture1, lecture2);
        Page<Lecture> page = new PageImpl<>(lectures, PageRequest.of(0, 1), 2);
        when(lectureService.findAll(PageRequest.of(0, 1))).thenReturn(page);

        mockMvc.perform(get("/api/lectures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ObjectListDto(lectures))))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(lectureService);
    }

    @Test
    public void whenGetOneLecture_thenOneLectureReturned() throws Exception {
        Lecture lecture = createLectureNoId();
        lecture.setId(1);
        when(lectureService.findById(lecture.getId())).thenReturn(lecture);

        mockMvc.perform(get("/api/lectures/{id}", lecture.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lectureMapper.lectureToDto(lecture))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveLecture_thenLectureSaved() throws Exception {
        Lecture lecture1 = createLectureNoId();
        Lecture lecture2 = createLectureNoId();
        lecture2.setId(2);
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture1);
        when(cathedraService.findByName(lectureDto.getCathedraName())).thenReturn(lecture1.getCathedra());
        when(teacherService.findByFirstNameAndLastNameAndBirthDate(lectureDto.getTeacherDto().getFirstName(), lectureDto.getTeacherDto().getLastName(), lectureDto.getTeacherDto().getBirthDate())).thenReturn(lecture1.getTeacher());
        when(audienceService.findByRoom(lectureDto.getAudienceRoom())).thenReturn(lecture1.getAudience());
        when(subjectService.findByName(lectureDto.getSubjectName())).thenReturn(lecture1.getSubject());
        when(lectureTimeService.findByStartAndEnd(lectureDto.getStart(), lectureDto.getEnd())).thenReturn(lecture1.getTime());
        when(lectureService.save(lecture1)).thenReturn(lecture2);
        when(groupService.findByName(lecture1.getGroups().get(0).getName())).thenReturn(lecture1.getGroups().get(0));

        mockMvc.perform(post("/api/lectures")
                .content(objectMapper.writeValueAsString(lectureDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/lectures/2"))
                .andExpect(status().isCreated());

        verify(lectureService).save(lecture1);
    }

    @Test
    public void whenEditLecture_thenLectureFound() throws Exception {
        Lecture lecture = createLectureNoId();
        lecture.setId(1);
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture);
        when(cathedraService.findByName(lectureDto.getCathedraName())).thenReturn(lecture.getCathedra());
        when(teacherService.findByFirstNameAndLastNameAndBirthDate(lectureDto.getTeacherDto().getFirstName(), lectureDto.getTeacherDto().getLastName(), lectureDto.getTeacherDto().getBirthDate())).thenReturn(lecture.getTeacher());
        when(audienceService.findByRoom(lectureDto.getAudienceRoom())).thenReturn(lecture.getAudience());
        when(subjectService.findByName(lectureDto.getSubjectName())).thenReturn(lecture.getSubject());
        when(lectureTimeService.findByStartAndEnd(lectureDto.getStart(), lectureDto.getEnd())).thenReturn(lecture.getTime());
        when(lectureService.save(lecture)).thenReturn(lecture);
        when(groupService.findByName(lecture.getGroups().get(0).getName())).thenReturn(lecture.getGroups().get(0));

        mockMvc.perform(patch("/api/lectures/{id}", 1)
                .content(objectMapper.writeValueAsString(lectureDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteLecture_thenLectureDeleted() throws Exception {
        Lecture lecture = createLectureNoId();
        lecture.setId(1);
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture);
        when(cathedraService.findByName(lectureDto.getCathedraName())).thenReturn(lecture.getCathedra());
        when(teacherService.findByFirstNameAndLastNameAndBirthDate(lectureDto.getTeacherDto().getFirstName(), lectureDto.getTeacherDto().getLastName(), lectureDto.getTeacherDto().getBirthDate())).thenReturn(lecture.getTeacher());
        when(audienceService.findByRoom(lectureDto.getAudienceRoom())).thenReturn(lecture.getAudience());
        when(subjectService.findByName(lectureDto.getSubjectName())).thenReturn(lecture.getSubject());
        when(lectureTimeService.findByStartAndEnd(lectureDto.getStart(), lectureDto.getEnd())).thenReturn(lecture.getTime());
        when(groupService.findByName(lecture.getGroups().get(0).getName())).thenReturn(lecture.getGroups().get(0));

        mockMvc.perform(delete("/api/lectures/{id}", 1)
                .content(objectMapper.writeValueAsString(lectureDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(lectureService).delete(lecture);
    }

    private Lecture createLectureNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience = Audience.builder().id(1).room(1).capacity(10).build();
        Group group = Group.builder().id(1).name("Group Name").cathedra(cathedra).build();
        Subject subject = Subject.builder().id(1).name("Subject name").description("Subject desc").cathedra(cathedra)
                .build();
        Teacher teacher = Teacher.builder().id(1).firstName("Test Name").lastName("Last Name").cathedra(cathedra).gender(Gender.MALE)
                .subjects(Arrays.asList(subject)).build();
        LectureTime time = LectureTime.builder().id(1).start(LocalTime.of(8, 0)).end(LocalTime.of(9, 45)).build();

        return Lecture.builder()
                .audience(audience)
                .cathedra(cathedra)
                .date(LocalDate.of(2021, 1, 1))
                .group(Arrays.asList(group))
                .subject(subject)
                .teacher(teacher)
                .time(time)
                .build();
    }
}