package com.foxminded.university.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.service.AudienceService;

@ExtendWith(MockitoExtension.class)
public class AudienceControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
	private AudienceService audienceService;
	@InjectMocks
	private AudienceController audienceController;
	
	@BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(audienceController).build();
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
		Audience audience3 = Audience.builder()
				.id(3)
				.room(1)
				.capacity(10)
				.cathedra(cathedra)
				.build();
		List<Audience> audiences = Arrays.asList(audience1, audience2, audience3);
		
        when(audienceService.findAll()).thenReturn(audiences);
 
        mockMvc.perform(get("/audiences"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/index"))
                .andExpect(forwardedUrl("audiences/index"))
                .andExpect(model().attribute("audiences", hasSize(3)))
                .andExpect(model().attribute("audiences", audiences));
 
        verify(audienceService, times(1)).findAll();
        verifyNoMoreInteractions(audienceService);
    }
}