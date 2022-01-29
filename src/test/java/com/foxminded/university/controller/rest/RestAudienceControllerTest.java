package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.service.AudienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestAudienceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AudienceService audienceService;
    @InjectMocks
    private RestAudienceController restAudienceController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restAudienceController).build();
    }

    @Test
    public void whenGetAllAudiences_thenAllAudiencesReturned() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience1 = Audience.builder()
            .id(1)
            .room(1)
            .capacity(10)
            .cathedra(cathedra)
            .build();
        Audience audience2 = Audience.builder()
            .id(2)
            .room(2)
            .capacity(30)
            .cathedra(cathedra)
            .build();
        List<Audience> audiences = Arrays.asList(audience1, audience2);
        when(audienceService.findAll()).thenReturn(audiences);

        mockMvc.perform(get("/api/audiences"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].room", is(1)))
            .andExpect(jsonPath("$[0].capacity", is(10)))
            .andExpect(jsonPath("$[0].cathedra.id", is(1)))
            .andExpect(jsonPath("$[0].cathedra.name", is("Fantastic Cathedra")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].room", is(2)))
            .andExpect(jsonPath("$[1].capacity", is(30)))
            .andExpect(jsonPath("$[1].cathedra.id", is(1)))
            .andExpect(jsonPath("$[1].cathedra.name", is("Fantastic Cathedra")));

        verifyNoMoreInteractions(audienceService);
    }

    @Test
    public void whenGetOneAudience_thenOneAudienceReturned() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience = Audience.builder()
            .id(1)
            .room(1)
            .capacity(10)
            .cathedra(cathedra)
            .build();
        when(audienceService.findById(audience.getId())).thenReturn(audience);

        mockMvc.perform(get("/api/audiences/{id}", audience.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.room", is(1)))
            .andExpect(jsonPath("$.capacity", is(10)))
            .andExpect(jsonPath("$.cathedra.id", is(1)))
            .andExpect(jsonPath("$.cathedra.name", is("Fantastic Cathedra")));
    }

    @Test
    void whenSaveAudience_thenAudienceSaved() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience = Audience.builder()
            .room(1)
            .capacity(10)
            .cathedra(cathedra)
            .build();
        mockMvc.perform(post("/api/audiences")
            .content(new ObjectMapper().writeValueAsBytes(audience))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(audienceService).save(audience);
    }

    @Test
    void whenEditAudience_thenAudienceFound() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience actual = Audience.builder()
            .id(1)
            .room(1)
            .capacity(10)
            .cathedra(cathedra)
            .build();

        mockMvc.perform(patch("/api/audiences/{id}", 1)
            .content(new ObjectMapper().writeValueAsBytes(actual))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(audienceService).save(actual);
    }

    @Test
    void whenDeleteAudience_thenAudienceDeleted() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience = Audience.builder()
            .id(1)
            .room(1)
            .capacity(10)
            .cathedra(cathedra)
            .build();
        mockMvc.perform(delete("/api/audiences/{id}", 1)
            .content(new ObjectMapper().writeValueAsBytes(audience))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(audienceService).delete(audience);
    }
}