package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.LectureTimeMapper;
import com.foxminded.university.dto.LectureTimeDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.LectureTime;
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

import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class LectureTimeSystemTest {

    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11");
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LectureTimeMapper lectureTimeMapper;

    @Test
    public void whenGetAllLectureTimes_thenAllLectureTimesReturned() {
        Slice<LectureTimeDto> actual = restTemplate.exchange("/api/lecturetimes/",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Slice<LectureTimeDto>>() {
            }).getBody();
        LectureTime lectureTime1 = createLectureTimeNoId();
        lectureTime1.setId(1);
        LectureTime lectureTime2 = createLectureTimeNoId();
        lectureTime2.setId(2);
        LectureTimeDto lectureTime1Dto = lectureTimeMapper.lectureTimeToDto(lectureTime1);
        LectureTimeDto lectureTime2Dto = lectureTimeMapper.lectureTimeToDto(lectureTime2);
        Slice<LectureTimeDto> expected = new Slice<>(Arrays.asList(lectureTime1Dto, lectureTime2Dto));

        assertEquals(expected, actual);
    }

    @Test
    public void whenGetOneLectureTime_thenOneLectureTimeReturned() {
        LectureTimeDto actual = restTemplate.getForObject("/api/lecturetimes/{id}", LectureTimeDto.class, 1);
        LectureTime lectureTime = createLectureTimeNoId();
        lectureTime.setId(1);
        LectureTimeDto expected = lectureTimeMapper.lectureTimeToDto(lectureTime);

        assertEquals(expected, actual);
    }

    @Test
    void whenFindNotExistingLectureTime_thenALectureTimeNotFound() {
        ResponseEntity<String> lectureTimeResponse = restTemplate.getForEntity("/api/lecturetimes/{id}", String.class, 67);

        assertEquals(HttpStatus.NOT_FOUND, lectureTimeResponse.getStatusCode());
    }

    @Test
    public void whenSaveLectureTime_thenLectureTimeSaved() {
        LectureTime lectureTime = createLectureTimeNoId();
        lectureTime.setStart(LocalTime.of(7, 0));
        LectureTimeDto lectureTimeDto = lectureTimeMapper.lectureTimeToDto(lectureTime);
        ResponseEntity<String> audienceResponse = restTemplate.postForEntity("/api/lecturetimes/", lectureTimeDto, String.class);

        assertEquals(HttpStatus.CREATED, audienceResponse.getStatusCode());

        lectureTimeDto.setId(9);
        Slice<LectureTimeDto> actual = restTemplate.exchange("/api/lecturetimes/",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Slice<LectureTimeDto>>() {
            }).getBody();
        LectureTime lectureTime1 = createLectureTimeNoId();
        lectureTime1.setId(1);
        LectureTime lectureTime2 = createLectureTimeNoId();
        lectureTime2.setId(2);
        LectureTimeDto lectureTime1Dto = lectureTimeMapper.lectureTimeToDto(lectureTime1);
        LectureTimeDto lectureTime2Dto = lectureTimeMapper.lectureTimeToDto(lectureTime2);
        Slice<LectureTimeDto> expected = new Slice<>(Arrays.asList(lectureTime1Dto, lectureTime2Dto, lectureTimeDto));

        assertEquals(expected, actual);
    }

    @Test
    public void whenEditLectureTime_thenLectureTimeFound() {
        LectureTime lectureTime = createLectureTimeNoId();
        lectureTime.setId(1);
        lectureTime.setStart(LocalTime.of(7, 0));
        LectureTimeDto lectureTimeDto = lectureTimeMapper.lectureTimeToDto(lectureTime);
        HttpEntity<LectureTimeDto> lectureTimeHttpEntity = new HttpEntity<>(lectureTimeDto);
        ResponseEntity<String> lectureTimeResponse = restTemplate.exchange("/api/lecturetimes/{id}?_method=patch", HttpMethod.POST, lectureTimeHttpEntity, String.class, 1);

        assertEquals(lectureTimeResponse.getStatusCode(), HttpStatus.OK);

        LectureTimeDto updatedLectureTime = restTemplate.getForObject("/api/lecturetimes/{id}", LectureTimeDto.class, 1);

        assertEquals(lectureTimeDto, updatedLectureTime);
    }

    @Test
    public void whenDeleteLectureTime_thenLectureTimeDeleted() {
        restTemplate.delete("/api/lecturetimes/{id}", 2);
        ResponseEntity<String> lectureTimeResponse = restTemplate.getForEntity("/lecturetimes/{id}", String.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, lectureTimeResponse.getStatusCode());
    }

    private LectureTime createLectureTimeNoId() {
        return LectureTime.builder()
            .start(LocalTime.of(8, 0))
            .end(LocalTime.of(9, 0))
            .build();
    }
}
