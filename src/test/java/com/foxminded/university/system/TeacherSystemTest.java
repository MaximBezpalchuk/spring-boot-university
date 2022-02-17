package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.model.*;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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
public class TeacherSystemTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private SubjectMapper subjectMapper = new SubjectMapperImpl(cathedraMapper);
    private TeacherMapper teacherMapper = new TeacherMapperImpl(cathedraMapper, subjectMapper);

    @Test
    public void whenGetAllTeachers_thenAllTeachersReturned() throws Exception {
        Teacher teacher1 = createTeacherNoId();
        teacher1.setId(1);
        Teacher teacher2 = createTeacherNoId();
        teacher2.setId(2);
        teacher2.setFirstName("FirstName1");
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        List<TeacherDto> teacherDtos = teachers.stream().map(teacherMapper::teacherToDto).collect(Collectors.toList());
        Page<TeacherDto> pageDtos = new PageImpl<>(teacherDtos, PageRequest.of(0, 3), 1);

        mockMvc.perform(get("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
            .andExpect(status().isOk());
    }

    @Test
    public void whenGetOneTeacher_thenOneTeacherReturned() throws Exception {
        Teacher teacher = createTeacherNoId();
        teacher.setId(1);
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher);

        mockMvc.perform(get("/api/teachers/{id}", teacher.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(teacherDto)))
            .andExpect(status().isOk());
    }

    @Test
    public void whenSaveTeacher_thenTeacherSaved() throws Exception {
        Teacher teacher = createTeacherNoId();
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher);

        mockMvc.perform(post("/api/teachers")
            .content(objectMapper.writeValueAsString(teacherDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    public void whenEditTeacher_thenTeacherFound() throws Exception {
        Teacher teacher = createTeacherNoId();
        teacher.setId(1);
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher);

        mockMvc.perform(patch("/api/teachers/{id}", 1)
            .content(objectMapper.writeValueAsString(teacherDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteTeacher_thenTeacherDeleted() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}", 1)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    private Teacher createTeacherNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Test Name").description("Desc").cathedra(cathedra).build();

        return Teacher.builder()
            .firstName("FirstName")
            .lastName("LastName")
            .phone("8888")
            .address("abc")
            .email("1@a.com")
            .gender(Gender.MALE)
            .postalCode("123")
            .education("high")
            .birthDate(LocalDate.of(1990, 1, 1))
            .cathedra(cathedra)
            .subjects(Arrays.asList(subject))
            .gender(Gender.MALE)
            .degree(Degree.PROFESSOR)
            .build();
    }
}
