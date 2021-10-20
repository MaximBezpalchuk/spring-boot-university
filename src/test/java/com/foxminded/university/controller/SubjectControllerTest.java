package com.foxminded.university.controller;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import com.foxminded.university.service.SubjectService;

@ExtendWith(MockitoExtension.class)
public class SubjectControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
	private SubjectService subjectService;
	@InjectMocks
	private SubjectController subjectController;
	
	@BeforeEach
    public void setMocks() {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).setCustomArgumentResolvers(resolver).build();
    }
	
	@Test
	public void whenGetAllSubjects_thenAllSubjectsReturned() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Subject subject1 = Subject.builder()
				.id(1)
				.name("Subject Name")
				.description("Subject desc")
				.cathedra(cathedra)
				.build();
		Subject subject2 = Subject.builder()
				.id(2)
				.name("Subject2 Name")
				.description("Subject2 desc")
				.cathedra(cathedra)
				.build();
		List<Subject> subjects = Arrays.asList(subject1, subject2);
		Page<Subject> page = new PageImpl<>(subjects, PageRequest.of(0, 1), 2);


		when(this.subjectService.findAll(isA(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/subjects"))
				.andExpect(status().isOk())
				.andExpect(view().name("subjects/index"))
				.andExpect(forwardedUrl("subjects/index"))
				.andExpect(model().attribute("subjects", page));

		verify(subjectService, times(1)).findAll(PageRequest.of(0, 1));
		verifyNoMoreInteractions(subjectService);
	}
}