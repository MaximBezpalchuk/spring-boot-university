package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.SubjectMapper;
import com.foxminded.university.dao.mapper.TeacherMapper;
import com.foxminded.university.dto.ObjectListDto;
import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.model.*;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TeacherRestControllerTest {

    private MockMvc mockMvc;
    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);
    private ObjectMapper objectMapper;
    @Mock
    private TeacherService teacherService;
    @Mock
    private CathedraService cathedraService;
    @Mock
    private SubjectService subjectService;
    @InjectMocks
    private TeacherRestController teacherRestController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 2));
        mockMvc = MockMvcBuilders.standaloneSetup(teacherRestController).setCustomArgumentResolvers(resolver).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ReflectionTestUtils.setField(teacherRestController, "teacherMapper", teacherMapper);
        ReflectionTestUtils.setField(teacherMapper, "cathedraService", cathedraService);
        ReflectionTestUtils.setField(teacherMapper, "subjectService", subjectService);
    }

    @Test
    public void whenGetAllTeachers_thenAllTeachersReturned() throws Exception {
        Teacher teacher1 = createTeacherNoId();
        teacher1.setId(1);
        Teacher teacher2 = createTeacherNoId();
        teacher2.setId(2);
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        Page<Teacher> page = new PageImpl<>(teachers, PageRequest.of(0, 2), 1);
        when(teacherService.findAll(isA(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ObjectListDto(teachers))))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(teacherService);
    }

    @Test
    public void whenGetOneTeacher_thenOneTeacherReturned() throws Exception {
        Teacher teacher = createTeacherNoId();
        teacher.setId(1);
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        mockMvc.perform(get("/api/teachers/{id}", teacher.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacherMapper.teacherToDto(teacher))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveTeacher_thenTeacherSaved() throws Exception {
        Teacher teacher1 = createTeacherNoId();
        Teacher teacher2 = createTeacherNoId();
        teacher2.setId(2);
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher1);
        when(cathedraService.findByName(teacherDto.getCathedraName())).thenReturn(teacher1.getCathedra());
        when(subjectService.findByName(teacher1.getSubjects().get(0).getName())).thenReturn(teacher1.getSubjects().get(0));
        when(teacherService.save(teacher1)).thenReturn(teacher2);

        mockMvc.perform(post("/api/teachers")
                .content(objectMapper.writeValueAsString(teacherDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/teachers/2"))
                .andExpect(status().isCreated());

        verify(teacherService).save(teacher1);
    }

    @Test
    public void whenEditTeacher_thenTeacherFound() throws Exception {
        Teacher teacher = createTeacherNoId();
        teacher.setId(1);
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher);
        when(cathedraService.findByName(teacherDto.getCathedraName())).thenReturn(teacher.getCathedra());
        when(subjectService.findByName(teacher.getSubjects().get(0).getName())).thenReturn(teacher.getSubjects().get(0));
        when(teacherService.save(teacher)).thenReturn(teacher);

        mockMvc.perform(patch("/api/teachers/{id}", 1)
                .content(objectMapper.writeValueAsString(teacherDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teacherService).save(teacher);
    }

    @Test
    public void whenDeleteTeacher_thenTeacherDeleted() throws Exception {
        Teacher teacher = createTeacherNoId();
        teacher.setId(1);
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher);
        when(cathedraService.findByName(teacherDto.getCathedraName())).thenReturn(teacher.getCathedra());
        when(subjectService.findByName(teacher.getSubjects().get(0).getName())).thenReturn(teacher.getSubjects().get(0));

        mockMvc.perform(delete("/api/teachers/{id}", 1)
                .content(objectMapper.writeValueAsString(teacherDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teacherService).delete(teacher);
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