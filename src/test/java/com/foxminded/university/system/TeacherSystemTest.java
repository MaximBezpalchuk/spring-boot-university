package com.foxminded.university.system;

import com.foxminded.university.dao.mapper.TeacherMapper;
import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.model.*;
import com.foxminded.university.pagination.config.PaginatedResponse;
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
public class TeacherSystemTest {

    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11");
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    public void whenGetAllTeachers_thenAllTeachersReturned() {
        ParameterizedTypeReference<PaginatedResponse<TeacherDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<TeacherDto>>() {
        };
        ResponseEntity<PaginatedResponse<TeacherDto>> teacherResponse = restTemplate.exchange("/api/teachers/", HttpMethod.GET, null, responseType);
        List<TeacherDto> actualTeacherDtos = teacherResponse.getBody().getContent();
        Teacher teacher1 = createTeacherNoId();
        teacher1.setId(1);
        Teacher teacher2 = createTeacherNoId();
        teacher2.setId(2);
        teacher2.setFirstName("FirstName1");
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        List<TeacherDto> teacherDtos = teachers.stream().map(teacherMapper::teacherToDto).collect(Collectors.toList());

        assertEquals(teacherDtos, actualTeacherDtos);
    }

    @Test
    public void whenGetOneTeacher_thenOneTeacherReturned() {
        TeacherDto actual = restTemplate.getForObject("/api/teachers/{id}", TeacherDto.class, 1);
        Teacher teacher = createTeacherNoId();
        teacher.setId(1);
        TeacherDto expected = teacherMapper.teacherToDto(teacher);

        assertEquals(expected, actual);
    }

    @Test
    void whenFindNotExistingTeacher_thenTeacherNotFound() {
        ResponseEntity<String> teacherResponse = restTemplate.getForEntity("/api/teachers/{id}", String.class, 67);

        assertEquals(HttpStatus.NOT_FOUND, teacherResponse.getStatusCode());
    }

    @Test
    public void whenSaveTeacher_thenTeacherSaved() {
        Teacher teacher = createTeacherNoId();
        teacher.setFirstName("Any name");
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher);
        ResponseEntity<String> teacherResponse = restTemplate.postForEntity("/api/teachers/", teacherDto, String.class);

        assertEquals(HttpStatus.CREATED, teacherResponse.getStatusCode());

        ParameterizedTypeReference<PaginatedResponse<TeacherDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<TeacherDto>>() {
        };
        ResponseEntity<PaginatedResponse<TeacherDto>> result = restTemplate.exchange("/api/teachers/", HttpMethod.GET, null, responseType);
        List<TeacherDto> actualTeacherDtos = result.getBody().getContent();
        Teacher teacher1 = createTeacherNoId();
        teacher1.setId(1);
        Teacher teacher2 = createTeacherNoId();
        teacher2.setId(2);
        teacher2.setFirstName("FirstName1");
        teacher.setId(3);
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2, teacher);
        List<TeacherDto> teacherDtos = teachers.stream().map(teacherMapper::teacherToDto).collect(Collectors.toList());

        assertEquals(teacherDtos, actualTeacherDtos);
    }

    @Test
    public void whenEditTeacher_thenTeacherFound() {
        Teacher teacher = createTeacherNoId();
        teacher.setId(1);
        teacher.setFirstName("Any name");
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher);
        HttpEntity<TeacherDto> teacherHttpEntity = new HttpEntity<>(teacherDto);
        ResponseEntity<String> teacherResponse = restTemplate.exchange("/api/teachers/{id}?_method=patch", HttpMethod.POST, teacherHttpEntity, String.class, 1);

        assertEquals(teacherResponse.getStatusCode(), HttpStatus.OK);

        TeacherDto updatedTeachers = restTemplate.getForObject("/api/teachers/{id}", TeacherDto.class, 1);

        assertEquals(teacherDto, updatedTeachers);
    }

    @Test
    public void whenDeleteTeacher_thenTeacherDeleted() {
        restTemplate.delete("/api/teachers/{id}", 2);
        ResponseEntity<String> teacherResponse = restTemplate.getForEntity("/teachers/{id}", String.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, teacherResponse.getStatusCode());
    }

    private Teacher createTeacherNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Test Name").description("Desc").cathedra(cathedra).build();

        return Teacher.builder()
            .firstName("FirstName")
            .lastName("LastName")
            .phone("8888")
            .address("abc")
            .email("1@a.com")
            .gender(Gender.MALE)
            .postalCode("123")
            .education("high")
            .birthDate(LocalDate.of(1990, 1, 1))
            .cathedra(cathedra)
            .subjects(Arrays.asList(subject))
            .gender(Gender.MALE)
            .degree(Degree.PROFESSOR)
            .build();
    }
}
