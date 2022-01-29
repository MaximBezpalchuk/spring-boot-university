package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.*;
import com.foxminded.university.service.TeacherService;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestTeacherControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private RestTeacherController restTeacherController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 2));
        mockMvc = MockMvcBuilders.standaloneSetup(restTeacherController).setCustomArgumentResolvers(resolver).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
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
            .gender(Gender.MALE)
            .degree(Degree.PROFESSOR)
            .build();
        Teacher teacher2 = Teacher.builder()
            .id(2)
            .firstName("Name2")
            .lastName("Last name")
            .cathedra(cathedra)
            .subjects(Arrays.asList(subject))
            .gender(Gender.MALE)
            .degree(Degree.PROFESSOR)
            .build();
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        Page<Teacher> page = new PageImpl<>(teachers, PageRequest.of(0, 2), 1);
        when(teacherService.findAll(isA(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/teachers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(1)))
            .andExpect(jsonPath("$.content[0].firstName", is("Name")))
            .andExpect(jsonPath("$.content[0].lastName", is("Last name")))
            .andExpect(jsonPath("$.content[0].subjects[0].id", is(1)))
            .andExpect(jsonPath("$.content[0].subjects[0].name", is("Subject name")))
            .andExpect(jsonPath("$.content[0].cathedra.id", is(1)))
            .andExpect(jsonPath("$.content[0].cathedra.name", is("Fantastic Cathedra")))
            .andExpect(jsonPath("$.content[1].id", is(2)))
            .andExpect(jsonPath("$.content[1].firstName", is("Name2")))
            .andExpect(jsonPath("$.content[1].lastName", is("Last name")))
            .andExpect(jsonPath("$.content[1].subjects[0].id", is(1)))
            .andExpect(jsonPath("$.content[1].subjects[0].name", is("Subject name")))
            .andExpect(jsonPath("$.content[1].cathedra.id", is(1)))
            .andExpect(jsonPath("$.content[1].cathedra.name", is("Fantastic Cathedra")))
            .andExpect(jsonPath("$.totalElements", is(2)))
            .andExpect(jsonPath("$.pageable.paged", is(true)));
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
            .gender(Gender.MALE)
            .degree(Degree.PROFESSOR)
            .build();
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        mockMvc.perform(get("/api/teachers/{id}", teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("Name")))
            .andExpect(jsonPath("$.lastName", is("Last name")))
            .andExpect(jsonPath("$.subjects[0].id", is(1)))
            .andExpect(jsonPath("$.subjects[0].name", is("Subject name")))
            .andExpect(jsonPath("$.cathedra.id", is(1)))
            .andExpect(jsonPath("$.cathedra.name", is("Fantastic Cathedra")));
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
            .gender(Gender.MALE)
            .degree(Degree.PROFESSOR)
            .build();

        mockMvc.perform(post("/api/teachers")
            .content(objectMapper.writeValueAsString(teacher))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(teacherService).save(teacher);
    }

    @Test
    void whenEditTeacher_thenTeacherFound() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Subject name").build();
        Teacher teacher = Teacher.builder()
            .id(1)
            .firstName("Name")
            .lastName("Last name")
            .cathedra(cathedra)
            .subjects(Arrays.asList(subject))
            .gender(Gender.MALE)
            .degree(Degree.PROFESSOR)
            .build();

        mockMvc.perform(patch("/api/teachers/{id}", 1)
            .content(objectMapper.writeValueAsString(teacher))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void whenDeleteTeacher_thenTeacherDeleted() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Subject name").build();
        Teacher teacher = Teacher.builder()
            .id(1)
            .firstName("Name")
            .lastName("Last name")
            .cathedra(cathedra)
            .subjects(Arrays.asList(subject))
            .gender(Gender.MALE)
            .degree(Degree.PROFESSOR)
            .build();
        mockMvc.perform(delete("/api/teachers/{id}", 1)
            .content(objectMapper.writeValueAsString(teacher))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(teacherService).delete(teacher);
    }
}