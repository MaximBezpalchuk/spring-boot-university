package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.StudentService;
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

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
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
public class RestStudentControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private StudentService studentService;
    @InjectMocks
    private RestStudentController restStudentController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(restStudentController).setCustomArgumentResolvers(resolver).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void whenGetAllStudents_thenAllStudentsReturned() throws Exception {
        Group group = Group.builder().id(1).name("Killers").build();
        Student student1 = Student.builder()
            .id(1)
            .firstName("Name")
            .lastName("Last name")
            .group(group)
            .gender(Gender.MALE)
            .build();
        Student student2 = Student.builder()
            .id(2)
            .firstName("Name2")
            .lastName("Last name")
            .group(group)
            .gender(Gender.MALE)
            .build();
        List<Student> students = Arrays.asList(student1, student2);
        Page<Student> page = new PageImpl<>(students, PageRequest.of(0, 1), 2);
        when(studentService.findAll(isA(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/students"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(1)))
            .andExpect(jsonPath("$.content[0].firstName", is("Name")))
            .andExpect(jsonPath("$.content[0].lastName", is("Last name")))
            .andExpect(jsonPath("$.content[0].group.id", is(1)))
            .andExpect(jsonPath("$.content[0].group.name", is("Killers")))
            .andExpect(jsonPath("$.content[1].id", is(2)))
            .andExpect(jsonPath("$.content[1].firstName", is("Name2")))
            .andExpect(jsonPath("$.content[1].lastName", is("Last name")))
            .andExpect(jsonPath("$.content[1].group.id", is(1)))
            .andExpect(jsonPath("$.content[1].group.name", is("Killers")))
            .andExpect(jsonPath("$.totalElements", is(2)))
            .andExpect(jsonPath("$.pageable.paged", is(true)));

        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void whenGetOneStudent_thenOneStudentReturned() throws Exception {
        Group group = Group.builder().id(1).name("Killers").build();
        Student student = Student.builder()
            .id(1)
            .firstName("Name")
            .lastName("Last name")
            .group(group)
            .gender(Gender.MALE)
            .build();
        when(studentService.findById(student.getId())).thenReturn(student);

        mockMvc.perform(get("/api/students/{id}", student.getId()))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("Name")))
            .andExpect(jsonPath("$.lastName", is("Last name")))
            .andExpect(jsonPath("$.group.id", is(1)))
            .andExpect(jsonPath("$.group.name", is("Killers")));
    }

    @Test
    void whenSaveStudent_thenStudentSaved() throws Exception {
        Group group = Group.builder().id(1).name("Killers").build();
        Student student = Student.builder()
            .firstName("Name")
            .lastName("Last name")
            .group(group)
            .gender(Gender.MALE)
            .build();
        mockMvc.perform(post("/api/students")
            .content(objectMapper.writeValueAsString(student))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(studentService).save(student);
    }

    @Test
    void whenEditStudent_thenStudentFound() throws Exception {
        Group group = Group.builder().id(1).name("Killers").build();
        Student student = Student.builder()
            .id(1)
            .firstName("Name")
            .lastName("Last name")
            .group(group)
            .gender(Gender.MALE)
            .build();

        mockMvc.perform(patch("/api/students/{id}", 1)
            .content(objectMapper.writeValueAsString(student))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void whenDeleteStudent_thenStudentDeleted() throws Exception {
        Group group = Group.builder().id(1).name("Killers").build();
        Student student = Student.builder()
            .id(1)
            .firstName("Name")
            .lastName("Last name")
            .group(group)
            .gender(Gender.MALE)
            .build();

        mockMvc.perform(delete("/api/students/{id}", 1)
            .content(objectMapper.writeValueAsString(student))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(studentService).delete(student);
    }
}