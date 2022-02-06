package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import com.foxminded.university.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SubjectRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubjectService subjectService;
    @InjectMocks
    private SubjectRestController subjectRestController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(subjectRestController).setCustomArgumentResolvers(resolver).build();
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
        when(subjectService.findAll(isA(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/subjects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(1)))
            .andExpect(jsonPath("$.content[0].name", is("Subject Name")))
            .andExpect(jsonPath("$.content[0].description", is("Subject desc")))
            .andExpect(jsonPath("$.content[0].cathedra.id", is(1)))
            .andExpect(jsonPath("$.content[0].cathedra.name", is("Fantastic Cathedra")))
            .andExpect(jsonPath("$.content[1].id", is(2)))
            .andExpect(jsonPath("$.content[1].name", is("Subject2 Name")))
            .andExpect(jsonPath("$.content[1].description", is("Subject2 desc")))
            .andExpect(jsonPath("$.content[1].cathedra.id", is(1)))
            .andExpect(jsonPath("$.content[1].cathedra.name", is("Fantastic Cathedra")))
            .andExpect(jsonPath("$.totalElements", is(2)))
            .andExpect(jsonPath("$.pageable.paged", is(true)));
        verifyNoMoreInteractions(subjectService);
    }

    @Test
    public void whenGetOneSubject_thenOneSubjectReturned() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder()
            .id(1)
            .name("Subject Name")
            .description("Subject desc")
            .cathedra(cathedra)
            .build();
        when(subjectService.findById(subject.getId())).thenReturn(subject);

        mockMvc.perform(get("/api/subjects/{id}", subject.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Subject Name")))
            .andExpect(jsonPath("$.description", is("Subject desc")))
            .andExpect(jsonPath("$.cathedra.id", is(1)))
            .andExpect(jsonPath("$.cathedra.name", is("Fantastic Cathedra")));
    }

    @Test
    void whenSaveSubject_thenSubjectSaved() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder()
            .name("Subject Name")
            .description("Subject desc")
            .cathedra(cathedra)
            .build();

        mockMvc.perform(post("/api/subjects")
            .content(new ObjectMapper().writeValueAsBytes(subject))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(subjectService).save(subject);
    }

    @Test
    void whenEditSubject_thenSubjectFound() throws Exception {
        Cathedra cathedra = Cathedra.builder()
            .id(1)
            .name("Fantastic Cathedra")
            .build();
        Subject subject = Subject.builder()
            .id(1)
            .name("Subject Name")
            .description("Subject desc")
            .cathedra(cathedra)
            .build();

        mockMvc.perform(patch("/api/subjects/{id}", 1)
            .content(new ObjectMapper().writeValueAsBytes(subject))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
        ;
    }

    @Test
    void whenDeleteSubject_thenSubjectDeleted() throws Exception {
        Cathedra cathedra = Cathedra.builder()
            .id(1)
            .name("Fantastic Cathedra")
            .build();
        Subject subject = Subject.builder()
            .id(1)
            .name("Subject Name")
            .description("Subject desc")
            .cathedra(cathedra)
            .build();
        mockMvc.perform(delete("/api/subjects/{id}", 1)
            .content(new ObjectMapper().writeValueAsBytes(subject))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
        ;

        verify(subjectService).delete(subject);
    }
}