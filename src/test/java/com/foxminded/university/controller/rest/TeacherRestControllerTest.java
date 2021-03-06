package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.model.*;
import com.foxminded.university.service.TeacherService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TeacherRestControllerTest {

    private MockMvc mockMvc;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private SubjectMapper subjectMapper = new SubjectMapperImpl(cathedraMapper);
    @Spy
    private TeacherMapper teacherMapper = new TeacherMapperImpl(cathedraMapper, subjectMapper);
    private ObjectMapper objectMapper;
    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private TeacherRestController teacherRestController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 2));
        mockMvc = MockMvcBuilders.standaloneSetup(teacherRestController).setCustomArgumentResolvers(resolver).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void whenGetAllTeachers_thenAllTeachersReturned() throws Exception {
        Teacher teacher1 = createTeacherNoId();
        teacher1.setId(1);
        Teacher teacher2 = createTeacherNoId();
        teacher2.setId(2);
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        Page<Teacher> page = new PageImpl<>(teachers, PageRequest.of(0, 2), 1);
        List<TeacherDto> teacherDtos = teachers.stream().map(teacherMapper::teacherToDto).collect(Collectors.toList());
        Page<TeacherDto> pageDtos = new PageImpl<>(teacherDtos, PageRequest.of(0, 2), 1);
        when(teacherService.findAll(PageRequest.of(0, 2))).thenReturn(page);

        mockMvc.perform(get("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(teacherService);
    }

    @Test
    public void whenGetOneTeacher_thenOneTeacherReturned() throws Exception {
        Teacher teacher = createTeacherNoId();
        teacher.setId(1);
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher);
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        mockMvc.perform(get("/api/teachers/{id}", teacher.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(teacherDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveTeacher_thenTeacherSaved() throws Exception {
        Teacher teacher = createTeacherNoId();
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher);
        when(teacherService.save(teacher)).thenAnswer(I -> {
            teacher.setId(2);
            return teacher;
        });

        mockMvc.perform(post("/api/teachers")
                .content(objectMapper.writeValueAsString(teacherDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/teachers/2"))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenEditTeacher_thenTeacherFound() throws Exception {
        Teacher teacher = createTeacherNoId();
        teacher.setId(1);
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher);
        when(teacherService.save(teacher)).thenReturn(teacher);

        mockMvc.perform(patch("/api/teachers/{id}", 1)
                .content(objectMapper.writeValueAsString(teacherDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teacherService).save(teacher);
    }

    @Test
    public void whenDeleteTeacher_thenTeacherDeleted() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teacherService).delete(1);
    }

    private Teacher createTeacherNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Subject name").build();

        return Teacher.builder()
                .firstName("Name")
                .lastName("Last name")
                .cathedra(cathedra)
                .subjects(Arrays.asList(subject))
                .gender(Gender.MALE)
                .degree(Degree.PROFESSOR)
                .build();
    }
}