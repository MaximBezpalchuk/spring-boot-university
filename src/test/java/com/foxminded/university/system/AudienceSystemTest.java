package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dao.mapper.AudienceMapperImpl;
import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Testcontainers
public class AudienceSystemTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private AudienceMapper audienceMapper = new AudienceMapperImpl(cathedraMapper);

    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:11")// Создать контейнер из образа postgres:11
                    .withInitScript("schema.sql").withInitScript("data.sql").waitingFor(Wait.forListeningPort());

    @Test
    void whenRestGetAllAudiences_thenAllJsonAudiencesReturned() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        Audience audience1 = Audience.builder().id(1).room(1).capacity(10).cathedra(cathedra).build();
        Audience audience2 = Audience.builder().id(2).room(2).capacity(30).cathedra(cathedra).build();
        Audience audience3 = Audience.builder().id(3).room(3).capacity(10).cathedra(cathedra).build();
        List<Audience> audiences = Arrays.asList(audience1, audience2, audience3);
        List<AudienceDto> audienceDtos = audiences.stream().map(audienceMapper::audienceToDto).collect(Collectors.toList());

        mvc.perform(get("/api/audiences")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(new Slice(audienceDtos))))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetAllAudiences_thenAllHtmlAudiencesReturned() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        Audience audience1 = Audience.builder().id(1).room(1).capacity(10).cathedra(cathedra).build();
        Audience audience2 = Audience.builder().id(2).room(2).capacity(30).cathedra(cathedra).build();
        Audience audience3 = Audience.builder().id(3).room(3).capacity(10).cathedra(cathedra).build();
        List<Audience> audiences = Arrays.asList(audience1, audience2, audience3);

        mvc.perform(get("/audiences"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/index"))
                .andExpect(model().attribute("audiences", audiences));
    }
}
