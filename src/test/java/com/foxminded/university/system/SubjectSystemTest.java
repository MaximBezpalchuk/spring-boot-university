package com.foxminded.university.system;

import com.foxminded.university.dao.mapper.SubjectMapper;
import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class SubjectSystemTest {

    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11");
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SubjectMapper subjectMapper;

    @Test
    public void whenGetAllSubjects_thenAllSubjectsReturned() {
        ParameterizedTypeReference<PaginatedResponse<SubjectDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<SubjectDto>>() {
        };
        ResponseEntity<PaginatedResponse<SubjectDto>> subjectResponse = restTemplate.exchange("/api/subjects/", HttpMethod.GET, null, responseType);
        List<SubjectDto> actualSubjectDtos = subjectResponse.getBody().getContent();
        Subject subject1 = createSubjectNoId();
        subject1.setId(1);
        Subject subject2 = createSubjectNoId();
        subject2.setId(2);
        subject2.setName("Test Name2");
        List<Subject> subjects = Arrays.asList(subject1, subject2);
        List<SubjectDto> subjectDtos = subjects.stream().map(subjectMapper::subjectToDto).collect(Collectors.toList());

        assertEquals(subjectDtos, actualSubjectDtos);
    }

    @Test
    public void whenGetOneSubject_thenOneSubjectReturned() {
        SubjectDto actual = restTemplate.getForObject("/api/subjects/{id}", SubjectDto.class, 1);
        Subject subject = createSubjectNoId();
        subject.setId(1);
        SubjectDto expected = subjectMapper.subjectToDto(subject);

        assertEquals(expected, actual);
    }

    @Test
    void whenFindNotExistingSubject_thenSubjectNotFound() {
        ResponseEntity<String> subjectResponse = restTemplate.getForEntity("/api/subjects/{id}", String.class, 67);

        assertEquals(HttpStatus.NOT_FOUND, subjectResponse.getStatusCode());
    }

    @Test
    public void whenSaveSubject_thenSubjectSaved() {
        Subject subject = createSubjectNoId();
        subject.setName("Test Name3");
        SubjectDto subjectDto = subjectMapper.subjectToDto(subject);
        ResponseEntity<String> subjectResponse = restTemplate.postForEntity("/api/subjects/", subjectDto, String.class);

        assertEquals(HttpStatus.CREATED, subjectResponse.getStatusCode());

        ParameterizedTypeReference<PaginatedResponse<SubjectDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<SubjectDto>>() {
        };
        ResponseEntity<PaginatedResponse<SubjectDto>> result = restTemplate.exchange("/api/subjects/", HttpMethod.GET, null, responseType);
        List<SubjectDto> actualSubjectDtos = result.getBody().getContent();
        Subject subject1 = createSubjectNoId();
        subject1.setId(1);
        Subject subject2 = createSubjectNoId();
        subject2.setId(2);
        subject2.setName("Test Name2");
        subject.setId(4);
        List<Subject> subjects = Arrays.asList(subject1, subject2, subject);
        List<SubjectDto> subjectDtos = subjects.stream().map(subjectMapper::subjectToDto).collect(Collectors.toList());

        assertEquals(subjectDtos, actualSubjectDtos);
    }

    @Test
    public void whenEditSubject_thenSubjectFound() {
        Subject subject = createSubjectNoId();
        subject.setId(1);
        subject.setName("Any name");
        SubjectDto subjectDto = subjectMapper.subjectToDto(subject);
        HttpEntity<SubjectDto> subjectHttpEntity = new HttpEntity<>(subjectDto);
        ResponseEntity<String> subjectResponse = restTemplate.exchange("/api/subjects/{id}?_method=patch", HttpMethod.POST, subjectHttpEntity, String.class, 1);

        assertEquals(subjectResponse.getStatusCode(), HttpStatus.OK);

        SubjectDto updatedSubjects = restTemplate.getForObject("/api/subjects/{id}", SubjectDto.class, 1);

        assertEquals(subjectDto, updatedSubjects);
    }

    @Test
    public void whenDeleteSubject_thenSubjectDeleted() {
        restTemplate.delete("/api/subjects/{id}", 2);
        ResponseEntity<String> subjectResponse = restTemplate.getForEntity("/subjects/{id}", String.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, subjectResponse.getStatusCode());
    }

    private Subject createSubjectNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Subject.builder()
            .name("Test Name")
            .description("Desc")
            .cathedra(cathedra)
            .build();
    }
}
