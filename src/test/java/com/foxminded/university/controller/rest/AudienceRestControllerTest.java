package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.ObjectListDto;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.CathedraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AudienceRestControllerTest {

    private MockMvc mockMvc;
    private AudienceMapper audienceMapper = Mappers.getMapper(AudienceMapper.class);

    ObjectMapper objectMapper;
    @Mock
    private AudienceService audienceService;
    @Mock
    private CathedraService cathedraService;
    @InjectMocks
    private AudienceRestController audienceRestController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(audienceRestController).build();
        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(audienceRestController, "audienceMapper", audienceMapper);
        ReflectionTestUtils.setField(audienceMapper, "cathedraService", cathedraService);
    }

    @Test
    public void whenGetAllAudiences_thenAllAudiencesReturned() throws Exception {
        Audience audience1 = createAudienceNoId();
        audience1.setId(1);
        Audience audience2 = createAudienceNoId();
        audience2.setId(2);
        List<Audience> audiences = Arrays.asList(audience1, audience2);
        when(audienceService.findAll()).thenReturn(audiences);

        mockMvc.perform(get("/api/audiences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ObjectListDto(audiences))))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(audienceService);
    }

    @Test
    public void whenGetOneAudience_thenOneAudienceReturned() throws Exception {
        Audience audience = createAudienceNoId();
        audience.setId(1);
        when(audienceService.findById(audience.getId())).thenReturn(audience);

        mockMvc.perform(get("/api/audiences/{id}", audience.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(audienceMapper.audienceToDto(audience))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveAudience_thenAudienceSaved() throws Exception {
        Audience audience1 = createAudienceNoId();
        Audience audience2 = createAudienceNoId();
        audience2.setId(4);
        AudienceDto audienceDto = audienceMapper.audienceToDto(audience1);
        when(cathedraService.findByName(audienceDto.getCathedraName())).thenReturn(audience1.getCathedra());
        when(audienceService.save(audience1)).thenReturn(audience2);
        mockMvc.perform(post("/api/audiences")
                .content(objectMapper.writeValueAsString(audienceDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/audiences/4"))
                .andExpect(status().isCreated());

        verify(audienceService).save(audience1);
    }

    @Test
    public void whenEditAudience_thenAudienceFound() throws Exception {
        Audience audience = createAudienceNoId();
        audience.setId(1);
        AudienceDto audienceDto = audienceMapper.audienceToDto(audience);
        when(cathedraService.findByName(audienceDto.getCathedraName())).thenReturn(audience.getCathedra());
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
        when(cathedraService.findByName(audienceDto.getCathedraName())).thenReturn(audience.getCathedra());

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