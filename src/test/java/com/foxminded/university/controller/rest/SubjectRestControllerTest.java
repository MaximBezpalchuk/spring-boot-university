package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dao.mapper.SubjectMapper;
import com.foxminded.university.dao.mapper.SubjectMapperImpl;
import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import com.foxminded.university.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SubjectRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    @Spy
    private SubjectMapper subjectMapper = new SubjectMapperImpl(cathedraMapper);
    @Mock
    private SubjectService subjectService;
    @InjectMocks
    private SubjectRestController subjectRestController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(subjectRestController).setCustomArgumentResolvers(resolver).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void whenGetAllSubjects_thenAllSubjectsReturned() throws Exception {
        Subject subject1 = createSubjectNoId();
        subject1.setId(1);
        Subject subject2 = createSubjectNoId();
        subject2.setId(2);
        List<Subject> subjects = Arrays.asList(subject1, subject2);
        Page<Subject> page = new PageImpl<>(subjects, PageRequest.of(0, 1), 2);
        List<SubjectDto> subjectDtos = subjects.stream().map(subjectMapper::subjectToDto).collect(Collectors.toList());
        Page<SubjectDto> pageDtos = new PageImpl<>(subjectDtos, PageRequest.of(0, 1), 2);
        when(subjectService.findAll(PageRequest.of(0, 1))).thenReturn(page);

        mockMvc.perform(get("/api/subjects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(subjectService);
    }

    @Test
    public void whenGetOneSubject_thenOneSubjectReturned() throws Exception {
        Subject subject = createSubjectNoId();
        subject.setId(1);
        SubjectDto subjectDto = subjectMapper.subjectToDto(subject);
        when(subjectService.findById(subject.getId())).thenReturn(subject);

        mockMvc.perform(get("/api/subjects/{id}", subject.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(subjectDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveSubject_thenSubjectSaved() throws Exception {
        Subject subject = createSubjectNoId();
        SubjectDto subjectDto = subjectMapper.subjectToDto(subject);
        when(subjectService.save(subject)).thenAnswer(I -> {
            subject.setId(2);
            return subject;
        });

        mockMvc.perform(post("/api/subjects")
                .content(objectMapper.writeValueAsString(subjectDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/subjects/2"))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenEditSubject_thenSubjectFound() throws Exception {
        Subject subject = createSubjectNoId();
        subject.setId(2);
        SubjectDto subjectDto = subjectMapper.subjectToDto(subject);
        when(subjectService.save(subject)).thenReturn(subject);

        mockMvc.perform(patch("/api/subjects/{id}", 1)
                .content(objectMapper.writeValueAsString(subjectDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        ;
    }

    @Test
    public void whenDeleteSubject_thenSubjectDeleted() throws Exception {
        mockMvc.perform(delete("/api/subjects/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(subjectService).delete(1);
    }

    private Subject createSubjectNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Subject.builder()
                .name("Subject Name")
                .description("Subject desc")
                .cathedra(cathedra)
                .build();
    }
}