package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.SubjectMapper;
import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class SubjectSystemTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SubjectMapper subjectMapper;

    @Test
    public void whenGetAllSubjects_thenAllSubjectsReturned() throws Exception {
        Subject subject1 = createSubjectNoId();
        subject1.setId(1);
        Subject subject2 = createSubjectNoId();
        subject2.setId(2);
        subject2.setName("Test Name2");
        List<Subject> subjects = Arrays.asList(subject1, subject2);
        List<SubjectDto> subjectDtos = subjects.stream().map(subjectMapper::subjectToDto).collect(Collectors.toList());
        Page<SubjectDto> pageDtos = new PageImpl<>(subjectDtos, PageRequest.of(0, 3), 2);

        mockMvc.perform(get("/api/subjects")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
            .andExpect(status().isOk());
    }

    @Test
    public void whenGetOneSubject_thenOneSubjectReturned() throws Exception {
        Subject subject = createSubjectNoId();
        subject.setId(1);
        SubjectDto subjectDto = subjectMapper.subjectToDto(subject);

        mockMvc.perform(get("/api/subjects/{id}", subject.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(subjectDto)))
            .andExpect(status().isOk());
    }

    @Test
    public void whenSaveSubject_thenSubjectSaved() throws Exception {
        Subject subject = createSubjectNoId();
        subject.setName("Test Name3");
        SubjectDto subjectDto = subjectMapper.subjectToDto(subject);

        mockMvc.perform(post("/api/subjects")
            .content(objectMapper.writeValueAsString(subjectDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    public void whenEditSubject_thenSubjectFound() throws Exception {
        Subject subject = createSubjectNoId();
        subject.setId(2);
        SubjectDto subjectDto = subjectMapper.subjectToDto(subject);

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
    }

    private Subject createSubjectNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Subject.builder()
            .name("Test Name")
            .description("Desc")
            .cathedra(cathedra)
            .build();
    }
}
