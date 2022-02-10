package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.*;
import com.foxminded.university.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LectureRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper .class);
    private GroupMapper groupMapper = new GroupMapperImpl(cathedraMapper);
    private SubjectMapper subjectMapper = new SubjectMapperImpl(cathedraMapper);
    private TeacherMapper teacherMapper = new TeacherMapperImpl(cathedraMapper, subjectMapper);
    private AudienceMapper audienceMapper = new AudienceMapperImpl(cathedraMapper);
    private LectureTimeMapper lectureTimeMapper = Mappers.getMapper(LectureTimeMapper .class);
    @Spy
    private LectureMapper lectureMapper = new LectureMapperImpl(cathedraMapper, groupMapper, teacherMapper, audienceMapper, subjectMapper, lectureTimeMapper);
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
        Lecture lecture1 = createLectureNoId();
        lecture1.setId(1);
        Lecture lecture2 = createLectureNoId();
        lecture2.setId(2);
        List<Lecture> lectures = Arrays.asList(lecture1, lecture2);
        Page<Lecture> page = new PageImpl<>(lectures, PageRequest.of(0, 1), 2);
        List<LectureDto> lectureDtos = lectures.stream().map(lectureMapper::lectureToDto).collect(Collectors.toList());
        Page<LectureDto> pageDtos = new PageImpl<>(lectureDtos, PageRequest.of(0, 1), 2);
        when(lectureService.findAll(PageRequest.of(0, 1))).thenReturn(page);

        mockMvc.perform(get("/api/lectures")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(lectureService);
    }

    @Test
    public void whenGetOneLecture_thenOneLectureReturned() throws Exception {
        Lecture lecture = createLectureNoId();
        lecture.setId(1);
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture);
        when(lectureService.findById(lecture.getId())).thenReturn(lecture);

        mockMvc.perform(get("/api/lectures/{id}", lecture.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(lectureDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveLecture_thenLectureSaved() throws Exception {
        Lecture lecture1 = createLectureNoId();
        Lecture lecture2 = createLectureNoId();
        lecture2.setId(2);
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture1);
        when(lectureService.save(lecture1)).thenReturn(lecture2);

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
        when(lectureService.save(lecture)).thenReturn(lecture);

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
        Teacher teacher = Teacher.builder().id(1).firstName("TestName").lastName("TestLastName").phone("88005553535")
                .address("Address").email("one@mail.com").gender(Gender.MALE).postalCode("123").education("Edu")
                .birthDate(LocalDate.of(2020, 1, 1)).cathedra(cathedra).subjects(Arrays.asList(subject))
                .degree(Degree.PROFESSOR).build();
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