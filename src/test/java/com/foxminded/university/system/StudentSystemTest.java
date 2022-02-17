package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.StudentDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class StudentSystemTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private GroupMapper groupMapper = new GroupMapperImpl(cathedraMapper);
    private StudentMapper studentMapper = new StudentMapperImpl(groupMapper);

    @Test
    public void whenGetAllStudents_thenAllStudentsReturned() throws Exception {
        Student student1 = createStudentNoId();
        student1.setId(1);
        Student student2 = createStudentNoId();
        student2.setId(2);
        student2.setFirstName("FirstName1");
        List<Student> students = Arrays.asList(student1, student2);
        List<StudentDto> studentDtos = students.stream().map(studentMapper::studentToDto).collect(Collectors.toList());
        Page<StudentDto> pageDtos = new PageImpl<>(studentDtos, PageRequest.of(0, 3), 2);

        mockMvc.perform(get("/api/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetOneStudent_thenOneStudentReturned() throws Exception {
        Student student = createStudentNoId();
        student.setId(1);
        StudentDto studentDto = studentMapper.studentToDto(student);

        mockMvc.perform(get("/api/students/{id}", student.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveStudent_thenStudentSaved() throws Exception {
        Student student = createStudentNoId();
        student.setFirstName("Any Name");
        StudentDto studentDto = studentMapper.studentToDto(student);

        mockMvc.perform(post("/api/students")
                .content(objectMapper.writeValueAsString(studentDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenEditStudent_thenStudentFound() throws Exception {
        Student student = createStudentNoId();
        student.setId(1);
        StudentDto studentDto = studentMapper.studentToDto(student);

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
    }

    private Student createStudentNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Group group = Group.builder().id(1).name("Killers").cathedra(cathedra).build();

        return Student.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .phone("8888")
                .address("abc")
                .email("1@a.com")
                .gender(Gender.MALE)
                .postalCode("123")
                .education("high")
                .birthDate(LocalDate.of(1990,1,1))
                .group(group)
                .build();
    }
}
