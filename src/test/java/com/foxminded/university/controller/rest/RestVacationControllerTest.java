package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.*;
import com.foxminded.university.service.VacationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestVacationControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private VacationService vacationService;
    @InjectMocks
    private RestVacationController restVacationController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(restVacationController).setCustomArgumentResolvers(resolver).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void whenGetAllVacations_thenAllVacationsReturned() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).gender(Gender.MALE)
            .build();
        Vacation vacation1 = Vacation.builder()
            .id(1)
            .teacher(teacher)
            .start(LocalDate.of(2021, 1, 1))
            .end(LocalDate.of(2021, 1, 2))
            .build();
        Vacation vacation2 = Vacation.builder()
            .id(1)
            .teacher(teacher)
            .start(LocalDate.of(2020, 1, 1))
            .end(LocalDate.of(2020, 1, 2))
            .build();
        List<Vacation> vacations = Arrays.asList(vacation1, vacation2);
        Page<Vacation> page = new PageImpl<>(vacations, PageRequest.of(0, 1), 2);
        when(vacationService.findByTeacherId(PageRequest.of(0, 1), teacher.getId())).thenReturn(page);

        mockMvc.perform(get("/api/teachers/{id}/vacations", teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(1)))
            .andExpect(jsonPath("$.content[0].start", is("2021-01-01")))
            .andExpect(jsonPath("$.content[0].end", is("2021-01-02")))
            .andExpect(jsonPath("$.content[0].teacher.firstName", is("Name")))
            .andExpect(jsonPath("$.content[0].teacher.lastName", is("Last name")))
            .andExpect(jsonPath("$.content[1].id", is(1)))
            .andExpect(jsonPath("$.content[1].start", is("2020-01-01")))
            .andExpect(jsonPath("$.content[1].end", is("2020-01-02")))
            .andExpect(jsonPath("$.content[1].teacher.firstName", is("Name")))
            .andExpect(jsonPath("$.content[1].teacher.lastName", is("Last name")))
            .andExpect(jsonPath("$.totalElements", is(2)))
            .andExpect(jsonPath("$.pageable.paged", is(true)));

        verifyNoMoreInteractions(vacationService);
    }

    @Test
    public void whenGetOneVacation_thenOneVacationReturned() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).gender(Gender.MALE)
            .build();
        Vacation vacation = Vacation.builder()
            .id(1)
            .teacher(teacher)
            .start(LocalDate.of(2021, 1, 1))
            .end(LocalDate.of(2021, 1, 2))
            .build();
        when(vacationService.findById(vacation.getId())).thenReturn(vacation);

        mockMvc.perform(get("/api/teachers/{teacherId}/vacations/{id}", teacher.getId(), vacation.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.start", is("2021-01-01")))
            .andExpect(jsonPath("$.end", is("2021-01-02")))
            .andExpect(jsonPath("$.teacher.firstName", is("Name")))
            .andExpect(jsonPath("$.teacher.lastName", is("Last name")));
    }

    @Test
    void whenSaveVacation_thenVacationSaved() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).gender(Gender.MALE)
            .build();
        Vacation vacation = Vacation.builder()
            .teacher(teacher)
            .start(LocalDate.of(2021, 1, 1))
            .end(LocalDate.of(2021, 1, 2))
            .build();
        mockMvc.perform(post("/api/teachers/{id}/vacations", teacher.getId())
            .content(objectMapper.writeValueAsString(vacation))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(vacationService).save(vacation);
    }

    @Test
    void whenEditVacation_thenVacationFound() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder()
            .id(1)
            .name("Weapon Tactics")
            .description("Learning how to use heavy weapon and guerrilla tactics")
            .cathedra(cathedra)
            .build();
        Teacher teacher = Teacher.builder()
            .id(1)
            .firstName("Daniel")
            .lastName("Morpheus")
            .phone("1")
            .address("Virtual Reality Capsule no 1")
            .email("1@bigowl.com")
            .gender(Gender.MALE)
            .postalCode("12345")
            .education("Higher education")
            .cathedra(cathedra)
            .degree(Degree.PROFESSOR)
            .birthDate(LocalDate.of(1970,1,1))
            .subjects(Arrays.asList(subject))
            .build();
        Vacation vacation = Vacation.builder()
            .id(1)
            .teacher(teacher)
            .start(LocalDate.of(2021, 1, 15))
            .end(LocalDate.of(2021, 1, 16))
            .build();

         mockMvc.perform(patch("/api/teachers/{id}/vacations/{vacId}", teacher.getId(), 1)
            .content(objectMapper.writeValueAsString(vacation))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void whenDeleteVacation_thenVacationDeleted() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).gender(Gender.MALE)
            .build();
        Vacation vacation = Vacation.builder()
            .id(1)
            .teacher(teacher)
            .start(LocalDate.of(2021, 1, 1))
            .end(LocalDate.of(2021, 1, 2))
            .build();

        mockMvc.perform(delete("/api/teachers/{id}/vacations/{vacId}", 1, 1)
            .content(objectMapper.writeValueAsString(vacation))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(vacationService).delete(vacation);
    }

    @Test
    void whenChangeTeacherOnLectures_thenTeachersFound() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).gender(Gender.MALE)
            .build();
        Lecture lecture = Lecture.builder().id(1).teacher(teacher).build();

        mockMvc.perform(get("/api/teachers/{id}/vacations/lectures?start=2021-04-04&end=2021-04-05", teacher.getId())
            .content(objectMapper.writeValueAsString(lecture))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}