package com.foxminded.university.system;

import com.foxminded.university.dao.mapper.HolidayMapper;
import com.foxminded.university.dto.HolidayDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
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
public class HolidaySystemTest {

    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11");
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private HolidayMapper holidayMapper;

    @Test
    public void whenGetAllHolidays_thenAllHolidaysReturned() {
        ParameterizedTypeReference<PaginatedResponse<HolidayDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<HolidayDto>>() {};
        ResponseEntity<PaginatedResponse<HolidayDto>> holidayResponse = restTemplate.exchange("/api/holidays/", HttpMethod.GET, null, responseType);
        List<HolidayDto> actualHolidayDtos = holidayResponse.getBody().getContent();
        Holiday holiday1 = createHolidayNoId();
        holiday1.setId(1);
        Holiday holiday2 = createHolidayNoId();
        holiday2.setId(2);
        holiday2.setName("Test Name2");
        List<Holiday> holidays = Arrays.asList(holiday1, holiday2);
        List<HolidayDto> holidayDtos = holidays.stream().map(holidayMapper::holidayToDto).collect(Collectors.toList());

        assertEquals(holidayDtos, actualHolidayDtos);
    }

    @Test
    public void whenGetOneHoliday_thenOneHolidayReturned() {
        HolidayDto actual = restTemplate.getForObject("/api/holidays/{id}", HolidayDto.class, 1);
        Holiday holiday = createHolidayNoId();
        holiday.setId(1);
        HolidayDto expected = holidayMapper.holidayToDto(holiday);

        assertEquals(expected, actual);
    }

    @Test
    void whenFindNotExistingHoliday_thenHolidayNotFound() {
        ResponseEntity<String> holidayResponse = restTemplate.getForEntity("/api/holidays/{id}", String.class, 67);

        assertEquals(HttpStatus.NOT_FOUND, holidayResponse.getStatusCode());
    }

    @Test
    public void whenSaveHoliday_thenHolidaySaved() {
        Holiday holiday = createHolidayNoId();
        holiday.setName("Any Name");
        HolidayDto holidayDto = holidayMapper.holidayToDto(holiday);
        ResponseEntity<String> holidayResponse = restTemplate.postForEntity("/api/holidays/", holidayDto, String.class);

        assertEquals(HttpStatus.CREATED, holidayResponse.getStatusCode());

        ParameterizedTypeReference<PaginatedResponse<HolidayDto>> responseType = new ParameterizedTypeReference<PaginatedResponse<HolidayDto>>() {};
        ResponseEntity<PaginatedResponse<HolidayDto>> result = restTemplate.exchange("/api/holidays/", HttpMethod.GET, null, responseType);
        List<HolidayDto> actualHolidayDtos = result.getBody().getContent();
        Holiday holiday1 = createHolidayNoId();
        holiday1.setId(1);
        Holiday holiday2 = createHolidayNoId();
        holiday2.setId(2);
        holiday2.setName("Test Name2");
        holiday.setId(7);
        List<Holiday> holidays = Arrays.asList(holiday1, holiday2, holiday);
        List<HolidayDto> holidayDtos = holidays.stream().map(holidayMapper::holidayToDto).collect(Collectors.toList());

        assertEquals(holidayDtos, actualHolidayDtos);
    }

    @Test
    public void whenEditHoliday_thenHolidayFound() {
        Holiday holiday = createHolidayNoId();
        holiday.setId(1);
        holiday.setName("Any name");
        HolidayDto holidayDto = holidayMapper.holidayToDto(holiday);
        HttpEntity<HolidayDto> holidayHttpEntity = new HttpEntity<>(holidayDto);
        ResponseEntity<String> holidayResponse = restTemplate.exchange("/api/holidays/{id}?_method=patch", HttpMethod.POST, holidayHttpEntity, String.class, 1);

        assertEquals(holidayResponse.getStatusCode(), HttpStatus.OK);

        HolidayDto updatedHolidays = restTemplate.getForObject("/api/holidays/{id}", HolidayDto.class, 1);

        assertEquals(holidayDto, updatedHolidays);
    }

    @Test
    public void whenDeleteHoliday_thenHolidayDeleted() {
        restTemplate.delete("/api/holidays/{id}", 2);
        ResponseEntity<String> holidayResponse = restTemplate.getForEntity("/holidays/{id}", String.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, holidayResponse.getStatusCode());
    }

    private Holiday createHolidayNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Holiday.builder().name("Test Name").date(LocalDate.of(2021, 1, 1)).cathedra(cathedra).build();
    }
}
