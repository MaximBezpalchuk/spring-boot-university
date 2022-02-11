package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.StudentDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
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

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StudentRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private GroupMapper groupMapper = new GroupMapperImpl(cathedraMapper);
    @Spy
    private StudentMapper studentMapper = new StudentMapperImpl(groupMapper);
    @Mock
    private StudentService studentService;
    @Mock
    private EntityManager em;
    @InjectMocks
    private StudentRestController studentRestController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(studentRestController).setCustomArgumentResolvers(resolver).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void whenGetAllStudents_thenAllStudentsReturned() throws Exception {
        Student student1 = createStudentNoId();
        student1.setId(1);
        Student student2 = createStudentNoId();
        student2.setId(2);
        List<Student> students = Arrays.asList(student1, student2);
        Page<Student> page = new PageImpl<>(students, PageRequest.of(0, 1), 2);
        List<StudentDto> studentDtos = students.stream().map(studentMapper::studentToDto).collect(Collectors.toList());
        Page<StudentDto> pageDtos = new PageImpl<>(studentDtos, PageRequest.of(0, 1), 2);
        when(studentService.findAll(PageRequest.of(0, 1))).thenReturn(page);

        mockMvc.perform(get("/api/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void whenGetOneStudent_thenOneStudentReturned() throws Exception {
        Student student = createStudentNoId();
        student.setId(1);
        StudentDto studentDto = studentMapper.studentToDto(student);
        when(studentService.findById(student.getId())).thenReturn(student);

        mockMvc.perform(get("/api/students/{id}", student.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveStudent_thenStudentSaved() throws Exception {
        Student student = createStudentNoId();
        StudentDto studentDto = studentMapper.studentToDto(student);

        when(studentService.save(student)).thenAnswer(I -> {
            student.setId(2);
            return student;
        });

        mockMvc.perform(post("/api/students")
                .content(objectMapper.writeValueAsString(studentDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/students/2"))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenEditStudent_thenStudentFound() throws Exception {
        Student student = createStudentNoId();
        student.setId(1);
        StudentDto studentDto = studentMapper.studentToDto(student);
        when(studentService.save(student)).thenReturn(student);

        mockMvc.perform(patch("/api/students/{id}", 1)
                .content(objectMapper.writeValueAsString(studentDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteStudent_thenStudentDeleted() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(studentService).delete(1);
    }

    private Student createStudentNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Group group = Group.builder().id(1).name("Killers").cathedra(cathedra).build();

        return Student.builder()
                .firstName("Name")
                .lastName("Last name")
                .group(group)
                .gender(Gender.MALE)
                .build();
    }
}