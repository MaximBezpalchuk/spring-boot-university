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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.SubjectService;

@ExtendWith(MockitoExtension.class)
public class SubjectControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
	private SubjectService subjectService;
	@Mock
	private CathedraService cathedraService;
	@InjectMocks
	private SubjectController subjectController;
	
	@BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }
	
	@Test
	public void whenGetAllSubjects_thenAllSubjectsReturned() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Subject first = Subject.builder()
				.id(1)
				.name("Subject Name")
				.description("Subject desc")
				.cathedra(cathedra)
				.build();
		Subject second = Subject.builder()
				.id(2)
				.name("Subject2 Name")
				.description("Subject2 desc")
				.cathedra(cathedra)
				.build();
		List<Subject> list = Arrays.asList(first, second);
		Page<Subject> subjectPage = new PageImpl<>(list, PageRequest.of(0, 1), 2);

		when(subjectService.findPaginatedSubjects(PageRequest.of(0, 1))).thenReturn(subjectPage);

		mockMvc.perform(get("/subjects?size=1"))
				.andExpect(status().isOk())
				.andExpect(view().name("subjects/index"))
				.andExpect(forwardedUrl("subjects/index"))
				.andExpect(model().attribute("subjectPage", subjectPage))
				.andExpect(model().attribute("pageNumbers", Arrays.asList(1, 2)));

		verify(subjectService, times(1)).findPaginatedSubjects(PageRequest.of(0, 1));
		verifyNoMoreInteractions(subjectService);
	}
	
	@Test
	void whenCreateNewSubject_thenNewSubjectCreated() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		
		when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
		
		mockMvc.perform(get("/subjects/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("subjects/new"))
				.andExpect(forwardedUrl("subjects/new"))
				.andExpect(model().attribute("subject", instanceOf(Subject.class)));
	}
	
	@Test
	void whenSaveSubject_thenSubjectSaved() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Subject subject = Subject.builder()
				.name("Subject Name")
				.description("Subject desc")
				.cathedra(cathedra)
				.build();
		
		mockMvc.perform(post("/subjects").flashAttr("subject", subject))		
				.andExpect(redirectedUrl("/subjects"));
		
		verify(subjectService).save(subject);
	}

	@Test
	void whenEditSubject_thenSubjectFound() throws Exception {
		Cathedra cathedra = Cathedra.builder()
				.id(1)
				.name("Fantastic Cathedra")
				.build();
		Subject expected = Subject.builder()
				.id(1)
				.name("Subject Name")
				.description("Subject desc")
				.cathedra(cathedra)
				.build();
		
		when(subjectService.findById(1)).thenReturn(expected);
		when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
		
		mockMvc.perform(get("/subjects/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("subjects/edit"))
				.andExpect(forwardedUrl("subjects/edit"))
				.andExpect(model().attribute("subject", is(expected)));
	}
	
	@Test
	void whenDeleteSubject_thenSubjectDeleted() throws Exception {
		mockMvc.perform(delete("/subjects/{id}", 1))
				.andExpect(redirectedUrl("/subjects"));
		
		verify(subjectService).deleteById(1);
	}
}