package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
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

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class AudienceSystemTest {

    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11");
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AudienceMapper audienceMapper;

    @Test
    void whenGetAllAudiences_thenAllAudiencesReturned() {
        Slice<AudienceDto> actual = restTemplate.exchange("/api/audiences/",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Slice<AudienceDto>>() {
            }).getBody();
        Audience audience1 = createAudienceNoId();
        audience1.setId(1);
        Audience audience2 = createAudienceNoId();
        audience2.setId(2);
        audience2.setRoom(2);
        AudienceDto audience1Dto = audienceMapper.audienceToDto(audience1);
        AudienceDto audience2Dto = audienceMapper.audienceToDto(audience2);
        Slice<AudienceDto> expected = new Slice<>(Arrays.asList(audience1Dto, audience2Dto));

        assertEquals(expected, actual);
    }

    @Test
    public void whenGetOneAudience_thenOneAudienceReturned() {
        AudienceDto actual = restTemplate.getForObject("/api/audiences/{id}", AudienceDto.class, 1);
        Audience audience = createAudienceNoId();
        audience.setId(1);
        AudienceDto expected = audienceMapper.audienceToDto(audience);

        assertEquals(expected, actual);
    }

    @Test
    void whenFindNotExistingAudience_thenAudienceNotFound() {
        ResponseEntity<String> audienceResponse = restTemplate.getForEntity("/api/audiences/{id}", String.class, 67);

        assertEquals(HttpStatus.NOT_FOUND, audienceResponse.getStatusCode());
    }

    @Test
    public void whenSaveAudience_thenAudienceSaved() {
        Audience audience = createAudienceNoId();
        audience.setRoom(5);
        AudienceDto audienceDto = audienceMapper.audienceToDto(audience);
        ResponseEntity<String> audienceResponse = restTemplate.postForEntity("/api/audiences/", audienceDto, String.class);

        assertEquals(HttpStatus.CREATED, audienceResponse.getStatusCode());

        audienceDto.setId(4);
        Slice<AudienceDto> actual = restTemplate.exchange("/api/audiences/",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Slice<AudienceDto>>() {
            }).getBody();
        Audience audience1 = createAudienceNoId();
        audience1.setId(1);
        Audience audience2 = createAudienceNoId();
        audience2.setId(2);
        audience2.setRoom(2);
        AudienceDto audience1Dto = audienceMapper.audienceToDto(audience1);
        AudienceDto audience2Dto = audienceMapper.audienceToDto(audience2);
        Slice<AudienceDto> expected = new Slice<>(Arrays.asList(audience1Dto, audience2Dto, audienceDto));

        assertEquals(expected, actual);
    }

    @Test
    public void whenEditAudience_thenAudienceFound() {
        Audience audience = createAudienceNoId();
        audience.setId(1);
        audience.setRoom(123);
        AudienceDto audienceDto = audienceMapper.audienceToDto(audience);
        HttpEntity<AudienceDto> audienceHttpEntity = new HttpEntity<>(audienceDto);
        ResponseEntity<String> audienceResponse = restTemplate.exchange("/api/audiences/{id}?_method=patch", HttpMethod.POST, audienceHttpEntity, String.class, 1);

        assertEquals(audienceResponse.getStatusCode(), HttpStatus.OK);

        AudienceDto updatedAudience = restTemplate.getForObject("/api/audiences/{id}", AudienceDto.class, 1);

        assertEquals(audienceDto, updatedAudience);
    }

    @Test
    public void whenDeleteAudience_thenAudienceDeleted() {
        restTemplate.delete("/api/audiences/{id}", 2);
        ResponseEntity<String> audienceResponse = restTemplate.getForEntity("/audiences/{id}", String.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, audienceResponse.getStatusCode());
    }

    private Audience createAudienceNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Audience.builder().room(1).capacity(5).cathedra(cathedra).build();
    }
}
