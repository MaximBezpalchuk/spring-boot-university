package com.foxminded.university.controller;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
	public void setUp() {
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
				.andExpect(model().attribute("audiences", audiences));
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

		mockMvc.perform(get("/audiences/{id}", audience.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("audiences/show"))
				.andExpect(forwardedUrl("audiences/show"))
				.andExpect(model().attribute("audience", audience));
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