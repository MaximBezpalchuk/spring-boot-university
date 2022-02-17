package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dao.mapper.AudienceMapperImpl;
import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class AudienceSystemTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private AudienceMapper audienceMapper = new AudienceMapperImpl(cathedraMapper);

    @Test
    void whenGetAllAudiences_thenAllAudiencesReturned() throws Exception {
        Audience audience1 = createAudienceNoId();
        audience1.setId(1);
        Audience audience2 = createAudienceNoId();
        audience2.setId(2);
        audience2.setRoom(2);
        List<Audience> audiences = Arrays.asList(audience1, audience2);
        List<AudienceDto> audienceDtos = audiences.stream().map(audienceMapper::audienceToDto).collect(Collectors.toList());

        mockMvc.perform(get("/api/audiences")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(new Slice(audienceDtos))))
            .andExpect(status().isOk());
    }

    @Test
    public void whenGetOneAudience_thenOneAudienceReturned() throws Exception {
        Audience audience = createAudienceNoId();
        audience.setId(1);
        AudienceDto audienceDto = audienceMapper.audienceToDto(audience);

        mockMvc.perform(get("/api/audiences/{id}", audience.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(audienceDto)))
            .andExpect(status().isOk());
    }

    @Test
    public void whenSaveAudience_thenAudienceSaved() throws Exception {
        Audience audience = createAudienceNoId();
        audience.setRoom(5);
        AudienceDto audienceDto = audienceMapper.audienceToDto(audience);

        mockMvc.perform(post("/api/audiences")
            .content(objectMapper.writeValueAsString(audienceDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/audiences/4"))
            .andExpect(status().isCreated());
    }

    @Test
    public void whenEditAudience_thenAudienceFound() throws Exception {
        Audience audience = createAudienceNoId();
        audience.setId(1);
        AudienceDto audienceDto = audienceMapper.audienceToDto(audience);

        mockMvc.perform(patch("/api/audiences/{id}", 1)
            .content(objectMapper.writeValueAsString(audienceDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteAudience_thenAudienceDeleted() throws Exception {
        mockMvc.perform(delete("/api/audiences/{id}", 1)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    private Audience createAudienceNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Audience.builder().room(1).capacity(5).cathedra(cathedra).build();
    }
}
