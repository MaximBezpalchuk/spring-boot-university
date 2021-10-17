package com.foxminded.university.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.foxminded.university.service.CathedraService;

@ExtendWith(MockitoExtension.class)
public class AudienceControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
	private AudienceService audienceService;
	@Mock
	private CathedraService cathedraService;
	@InjectMocks
	private AudienceController audienceController;
	
	@BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(audienceController).build();
    }
	
	@Test
    public void whenGetAllAudiences_thenAllAudiencesReturned() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience first = Audience.builder()
				.id(1)
				.room(1)
				.capacity(10)
				.cathedra(cathedra)
				.build();
		Audience second = Audience.builder()
				.id(2)
				.room(2)
				.capacity(30)
				.cathedra(cathedra)
				.build();
		Audience third = Audience.builder()
				.id(3)
				.room(1)
				.capacity(10)
				.cathedra(cathedra)
				.build();
		List<Audience> list = Arrays.asList(first, second, third);
		
        when(audienceService.findAll()).thenReturn(list);
 
        mockMvc.perform(get("/audiences"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/index"))
                .andExpect(forwardedUrl("audiences/index"))
                .andExpect(model().attribute("audiences", hasSize(3)))
                .andExpect(model().attribute("audiences", list));
 
        verify(audienceService, times(1)).findAll();
        verifyNoMoreInteractions(audienceService);
    }
	
	@Test
	void whenCreateNewAudience_thenNewAudienceCreated() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		
		when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
		
		mockMvc.perform(get("/audiences/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("audiences/new"))
				.andExpect(forwardedUrl("audiences/new"))
				.andExpect(model().attribute("audience", instanceOf(Audience.class)));
	}
	
	@Test
	void whenSaveAudience_thenAudienceSaved() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience audience = Audience.builder()
				.room(1)
				.capacity(10)
				.cathedra(cathedra)
				.build();
		mockMvc.perform(post("/audiences").flashAttr("audience", audience))		
				.andExpect(redirectedUrl("/audiences"));
		
		verify(audienceService).save(audience);
	}

	@Test
	void whenEditAudience_thenAudienceFound() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Audience expected = Audience.builder()
				.room(1)
				.capacity(10)
				.cathedra(cathedra)
				.build();
		
		when(audienceService.findById(1)).thenReturn(expected);
		when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
		
		mockMvc.perform(get("/audiences/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("audiences/edit"))
				.andExpect(forwardedUrl("audiences/edit"))
				.andExpect(model().attribute("audience", is(expected)));
	}
	
	@Test
	void whenDeleteAudience_thenAudienceDeleted() throws Exception {
		mockMvc.perform(delete("/audiences/{id}", 1))
				.andExpect(redirectedUrl("/audiences"));
		
		verify(audienceService).deleteById(1);
	}
}