package com.foxminded.university.system;

import com.foxminded.university.dao.mapper.LectureMapper;
import com.foxminded.university.dto.LectureDto;
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
public class LectureSystemTest {

    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11");
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private LectureMapper lectureMapper;

    @Test
    public void whenGetAllLectures_thenAllLecturesReturned() {
        ParameterizedTypeReference<PaginatedResponse<LectureDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<LectureDto>>() {
        };
        ResponseEntity<PaginatedResponse<LectureDto>> lectureResponse = restTemplate.exchange("/api/lectures/", HttpMethod.GET, null, responseType);
        List<LectureDto> actualLectureDtos = lectureResponse.getBody().getContent();
        Lecture lecture1 = createLectureNoId();
        lecture1.setId(1);
        Lecture lecture2 = createLectureNoId();
        lecture2.setId(2);
        lecture2.setDate(LocalDate.of(2021, 1, 2));
        List<Lecture> lectures = Arrays.asList(lecture1, lecture2);
        List<LectureDto> lectureDtos = lectures.stream().map(lectureMapper::lectureToDto).collect(Collectors.toList());

        assertEquals(lectureDtos, actualLectureDtos);
    }

    @Test
    public void whenGetOneLecture_thenOneLectureReturned() {
        LectureDto actual = restTemplate.getForObject("/api/lectures/{id}", LectureDto.class, 1);
        Lecture lecture = createLectureNoId();
        lecture.setId(1);
        LectureDto expected = lectureMapper.lectureToDto(lecture);

        assertEquals(expected, actual);
    }

    @Test
    void whenFindNotExistingLecture_thenLectureNotFound() {
        ResponseEntity<String> lectureResponse = restTemplate.getForEntity("/api/lectures/{id}", String.class, 67);

        assertEquals(HttpStatus.NOT_FOUND, lectureResponse.getStatusCode());
    }

    @Test
    public void whenSaveLecture_thenLectureSaved() {
        Lecture lecture = createLectureNoId();
        lecture.setDate(LocalDate.of(2020, 1, 1));
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture);
        ResponseEntity<String> lectureResponse = restTemplate.postForEntity("/api/lectures/", lectureDto, String.class);

        assertEquals(HttpStatus.CREATED, lectureResponse.getStatusCode());

        ParameterizedTypeReference<PaginatedResponse<LectureDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<LectureDto>>() {
        };
        ResponseEntity<PaginatedResponse<LectureDto>> result = restTemplate.exchange("/api/lectures/", HttpMethod.GET, null, responseType);
        List<LectureDto> actualLectureDtos = result.getBody().getContent();
        Lecture lecture1 = createLectureNoId();
        lecture1.setId(1);
        Lecture lecture2 = createLectureNoId();
        lecture2.setId(2);
        lecture2.setDate(LocalDate.of(2021, 1, 2));
        lecture.setId(12);
        List<Lecture> lectures = Arrays.asList(lecture1, lecture2, lecture);
        List<LectureDto> lectureDtos = lectures.stream().map(lectureMapper::lectureToDto).collect(Collectors.toList());

        assertEquals(lectureDtos, actualLectureDtos);
    }

    @Test
    public void whenEditLecture_thenLectureFound() {
        Lecture lecture = createLectureNoId();
        lecture.setId(1);
        lecture.setDate(LocalDate.of(2021, 1, 1));
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture);
        HttpEntity<LectureDto> lectureHttpEntity = new HttpEntity<>(lectureDto);
        ResponseEntity<String> lectureResponse = restTemplate.exchange("/api/lectures/{id}?_method=patch", HttpMethod.POST, lectureHttpEntity, String.class, 1);

        assertEquals(lectureResponse.getStatusCode(), HttpStatus.OK);

        LectureDto updatedLectures = restTemplate.getForObject("/api/lectures/{id}", LectureDto.class, 1);

        assertEquals(lectureDto, updatedLectures);
    }

    @Test
    public void whenDeleteLecture_thenLectureDeleted() {
        restTemplate.delete("/api/lectures/{id}", 2);
        ResponseEntity<String> lectureResponse = restTemplate.getForEntity("/lectures/{id}", String.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, lectureResponse.getStatusCode());
    }

    private Lecture createLectureNoId() {
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

        return Lecture.builder()
            .audience(audience)
            .cathedra(cathedra)
            .date(LocalDate.of(2021, 1, 1))
            .groups(Arrays.asList(group))
            .subject(subject)
            .teacher(teacher)
            .time(time)
            .build();
    }
}
