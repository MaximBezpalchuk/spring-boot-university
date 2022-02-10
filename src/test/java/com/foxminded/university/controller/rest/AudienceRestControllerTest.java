package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dao.mapper.AudienceMapperImpl;
import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.service.AudienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AudienceRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    @Spy
    private AudienceMapper audienceMapper = new AudienceMapperImpl(cathedraMapper);
    @Mock
    private AudienceService audienceService;
    @InjectMocks
    private AudienceRestController audienceRestController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(audienceRestController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void whenGetAllAudiences_thenAllAudiencesReturned() throws Exception {
        Audience audience1 = createAudienceNoId();
        audience1.setId(1);
        Audience audience2 = createAudienceNoId();
        audience2.setId(2);
        List<Audience> audiences = Arrays.asList(audience1, audience2);
        List<AudienceDto> audienceDtos = audiences.stream().map(audienceMapper::audienceToDto).collect(Collectors.toList());
        when(audienceService.findAll()).thenReturn(audiences);

        mockMvc.perform(get("/api/audiences")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(new Slice(audienceDtos))))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(audienceService);
    }

    @Test
    public void whenGetOneAudience_thenOneAudienceReturned() throws Exception {
        Audience audience = createAudienceNoId();
        audience.setId(1);
        AudienceDto audienceDto = audienceMapper.audienceToDto(audience);
        when(audienceService.findById(audience.getId())).thenReturn(audience);

        mockMvc.perform(get("/api/audiences/{id}", audience.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(audienceDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveAudience_thenAudienceSaved() throws Exception {
        Audience audience = createAudienceNoId();
        AudienceDto audienceDto = audienceMapper.audienceToDto(audience);
        when(audienceService.save(audience)).thenAnswer(I -> {
            audience.setId(4);
            return audience;
        });
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
        when(audienceService.save(audience)).thenReturn(audience);

        mockMvc.perform(patch("/api/audiences/{id}", 1)
                .content(objectMapper.writeValueAsString(audienceDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(audienceService).save(audience);
    }

    @Test
    public void whenDeleteAudience_thenAudienceDeleted() throws Exception {
        Audience audience = createAudienceNoId();
        audience.setId(1);
        AudienceDto audienceDto = audienceMapper.audienceToDto(audience);

        mockMvc.perform(delete("/api/audiences/{id}", 1)
                .content(objectMapper.writeValueAsString(audienceDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(audienceService).delete(audience);
    }

    private Audience createAudienceNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Audience.builder().room(1).capacity(10).cathedra(cathedra).build();
    }
}