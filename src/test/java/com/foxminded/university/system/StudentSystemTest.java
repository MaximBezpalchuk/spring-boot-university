package com.foxminded.university.system;

import com.foxminded.university.dao.mapper.StudentMapper;
import com.foxminded.university.dto.StudentDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.paginationConfig.PaginatedResponse;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class StudentSystemTest {

    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11");
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void whenGetAllStudents_thenAllStudentsReturned() {
        ParameterizedTypeReference<PaginatedResponse<StudentDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<StudentDto>>() {
        };
        ResponseEntity<PaginatedResponse<StudentDto>> studentResponse = restTemplate.exchange("/api/students/", HttpMethod.GET, null, responseType);
        List<StudentDto> actualStudentDtos = studentResponse.getBody().getContent();
        Student student1 = createStudentNoId();
        student1.setId(1);
        Student student2 = createStudentNoId();
        student2.setId(2);
        student2.setFirstName("FirstName1");
        List<Student> students = Arrays.asList(student1, student2);
        List<StudentDto> studentDtos = students.stream().map(studentMapper::studentToDto).collect(Collectors.toList());

        assertEquals(studentDtos, actualStudentDtos);
    }

    @Test
    public void whenGetOneStudent_thenOneStudentReturned() {
        StudentDto actual = restTemplate.getForObject("/api/students/{id}", StudentDto.class, 1);
        Student student = createStudentNoId();
        student.setId(1);
        StudentDto expected = studentMapper.studentToDto(student);

        assertEquals(expected, actual);
    }

    @Test
    void whenFindNotExistingStudent_thenStudentNotFound() {
        ResponseEntity<String> studentResponse = restTemplate.getForEntity("/api/students/{id}", String.class, 67);

        assertEquals(HttpStatus.NOT_FOUND, studentResponse.getStatusCode());
    }

    @Test
    public void whenSaveStudent_thenStudentSaved() {
        Student student = createStudentNoId();
        student.setFirstName("Any Name");
        StudentDto studentDto = studentMapper.studentToDto(student);
        ResponseEntity<String> studentResponse = restTemplate.postForEntity("/api/students/", studentDto, String.class);

        assertEquals(HttpStatus.CREATED, studentResponse.getStatusCode());

        ParameterizedTypeReference<PaginatedResponse<StudentDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<StudentDto>>() {
        };
        ResponseEntity<PaginatedResponse<StudentDto>> result = restTemplate.exchange("/api/students/", HttpMethod.GET, null, responseType);
        List<StudentDto> actualStudentDtos = result.getBody().getContent();
        Student student1 = createStudentNoId();
        student1.setId(1);
        Student student2 = createStudentNoId();
        student2.setId(2);
        student2.setFirstName("FirstName1");
        student.setId(6);
        List<Student> students = Arrays.asList(student1, student2, student);
        List<StudentDto> studentDtos = students.stream().map(studentMapper::studentToDto).collect(Collectors.toList());

        assertEquals(studentDtos, actualStudentDtos);
    }

    @Test
    public void whenEditStudent_thenStudentFound() {
        Student student = createStudentNoId();
        student.setId(1);
        student.setFirstName("Aby name");
        StudentDto studentDto = studentMapper.studentToDto(student);
        HttpEntity<StudentDto> studentHttpEntity = new HttpEntity<>(studentDto);
        ResponseEntity<String> studentResponse = restTemplate.exchange("/api/students/{id}?_method=patch", HttpMethod.POST, studentHttpEntity, String.class, 1);

        assertEquals(studentResponse.getStatusCode(), HttpStatus.OK);

        StudentDto updatedStudents = restTemplate.getForObject("/api/students/{id}", StudentDto.class, 1);

        assertEquals(studentDto, updatedStudents);
    }

    @Test
    public void whenDeleteStudent_thenStudentDeleted() {
        restTemplate.delete("/api/students/{id}", 2);
        ResponseEntity<String> studentResponse = restTemplate.getForEntity("/students/{id}", String.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, studentResponse.getStatusCode());
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
            .birthDate(LocalDate.of(1990, 1, 1))
            .group(group)
            .build();
    }
}
