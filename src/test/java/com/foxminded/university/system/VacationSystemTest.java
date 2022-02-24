package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.LectureMapper;
import com.foxminded.university.dao.mapper.VacationMapper;
import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.dto.VacationDto;
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
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class VacationSystemTest {

    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11");
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LectureMapper lectureMapper;
    @Autowired
    private VacationMapper vacationMapper;

    @Test
    public void whenGetAllVacations_thenAllVacationsReturned() {
        ParameterizedTypeReference<PaginatedResponse<VacationDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<VacationDto>>() {
        };
        ResponseEntity<PaginatedResponse<VacationDto>> vacationResponse = restTemplate.exchange("/api/teachers/1/vacations", HttpMethod.GET, null, responseType);
        List<VacationDto> actualVacationDtos = vacationResponse.getBody().getContent();
        Vacation vacation1 = createVacationNoId();
        vacation1.setId(1);
        Vacation vacation2 = createVacationNoId();
        vacation2.setId(2);
        vacation2.setStart(LocalDate.of(2021, 1, 2));
        vacation2.setEnd(LocalDate.of(2021, 1, 3));
        List<Vacation> vacations = Arrays.asList(vacation1, vacation2);
        List<VacationDto> vacationDtos = vacations.stream().map(vacationMapper::vacationToDto).collect(Collectors.toList());

        assertEquals(vacationDtos, actualVacationDtos);
    }

    @Test
    public void whenGetOneVacation_thenOneVacationReturned() {
        VacationDto actual = restTemplate.getForObject("/api/teachers/1/vacations/{id}", VacationDto.class, 1);
        Vacation vacation = createVacationNoId();
        vacation.setId(1);
        VacationDto expected = vacationMapper.vacationToDto(vacation);

        assertEquals(expected, actual);
    }

    @Test
    void whenFindNotExistingVacation_thenVacationNotFound() {
        ResponseEntity<String> vacationResponse = restTemplate.getForEntity("/api/teachers/1/vacations/{id}", String.class, 67);

        assertEquals(HttpStatus.NOT_FOUND, vacationResponse.getStatusCode());
    }

    @Test
    public void whenSaveVacation_thenVacationSaved() {
        Vacation vacation = createVacationNoId();
        vacation.setStart(LocalDate.of(2020, 1, 1));
        vacation.setEnd(LocalDate.of(2020, 1, 3));
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation);
        ResponseEntity<String> vacationResponse = restTemplate.postForEntity("/api/teachers/1/vacations", vacationDto, String.class);

        assertEquals(HttpStatus.CREATED, vacationResponse.getStatusCode());

        ParameterizedTypeReference<PaginatedResponse<VacationDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<VacationDto>>() {
        };
        ResponseEntity<PaginatedResponse<VacationDto>> result = restTemplate.exchange("/api/teachers/1/vacations", HttpMethod.GET, null, responseType);
        List<VacationDto> actualVacationDtos = result.getBody().getContent();
        Vacation vacation1 = createVacationNoId();
        vacation1.setId(1);
        Vacation vacation2 = createVacationNoId();
        vacation2.setId(2);
        vacation2.setStart(LocalDate.of(2021, 1, 2));
        vacation2.setEnd(LocalDate.of(2021, 1, 3));
        vacation.setId(5);
        List<Vacation> vacations = Arrays.asList(vacation1, vacation2, vacation);
        List<VacationDto> vacationDtos = vacations.stream().map(vacationMapper::vacationToDto).collect(Collectors.toList());

        assertEquals(vacationDtos, actualVacationDtos);
    }

    @Test
    public void whenEditVacation_thenVacationFound() {
        Vacation vacation = createVacationNoId();
        vacation.setId(1);
        vacation.setStart(LocalDate.of(2020, 1, 1));
        vacation.setEnd(LocalDate.of(2020, 1, 3));
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation);
        HttpEntity<VacationDto> vacationHttpEntity = new HttpEntity<>(vacationDto);
        ResponseEntity<String> vacationResponse = restTemplate.exchange("/api/teachers/{id}/vacations/{vacId}?_method=patch", HttpMethod.POST, vacationHttpEntity, String.class, 1, 1);

        assertEquals(vacationResponse.getStatusCode(), HttpStatus.OK);

        VacationDto updatedVacations = restTemplate.getForObject("/api/teachers/{id}/vacations/{vacId}", VacationDto.class, 1, 1);

        assertEquals(vacationDto, updatedVacations);
    }

    @Test
    public void whenDeleteVacation_thenVacationDeleted() {
        restTemplate.delete("/api/teachers/{id}/vacations/{vacId}", 1, 1);
        ResponseEntity<String> vacationResponse = restTemplate.getForEntity("/api/teachers/{id}/vacations/{vacId}", String.class, 1, 1);

        assertEquals(HttpStatus.NOT_FOUND, vacationResponse.getStatusCode());
    }

    @Test
    public void whenChangeTeacherOnLectures_thenTeachersFound() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience = Audience.builder().id(1).room(1).capacity(5).cathedra(cathedra).build();
        Group group = Group.builder().id(1).name("Killers").cathedra(cathedra).build();
        Subject subject = Subject.builder().id(1).name("Test Name").description("Desc").cathedra(cathedra).build();
        Teacher teacher = Teacher.builder()
            .id(1)
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
        LectureTime time = LectureTime.builder()
            .id(1)
            .start(LocalTime.of(8, 0))
            .end(LocalTime.of(9, 0))
            .build();
        Lecture lecture = Lecture.builder()
            .id(1)
            .audience(audience)
            .cathedra(cathedra)
            .date(LocalDate.of(2021, 1, 1))
            .groups(Arrays.asList(group))
            .subject(subject)
            .teacher(teacher)
            .time(time)
            .build();
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture);

        Slice<LectureDto> actual = restTemplate.exchange("/api/teachers/{id}/vacations/lectures?start=2021-01-01&end=2021-01-02",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Slice<LectureDto>>() {
            }, teacher.getId()).getBody();

        assertEquals(new Slice<>(Arrays.asList(lectureDto)), actual);
    }

    private Vacation createVacationNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Test Name").description("Desc").cathedra(cathedra).build();
        Teacher teacher = Teacher.builder()
            .id(1)
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

        return Vacation.builder()
            .teacher(teacher)
            .start(LocalDate.of(2021, 1, 1))
            .end(LocalDate.of(2021, 1, 2))
            .build();
    }
}
