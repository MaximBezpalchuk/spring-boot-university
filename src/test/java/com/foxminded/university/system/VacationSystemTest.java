package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.LectureMapper;
import com.foxminded.university.dao.mapper.VacationMapper;
import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.dto.VacationDto;
import com.foxminded.university.model.*;
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

import java.time.LocalDate;
import java.time.LocalTime;
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
public class VacationSystemTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LectureMapper lectureMapper;
    @Autowired
    private VacationMapper vacationMapper;

    @Test
    public void whenGetAllVacations_thenAllVacationsReturned() throws Exception {
        Vacation vacation1 = createVacationNoId();
        vacation1.setId(1);
        Vacation vacation2 = createVacationNoId();
        vacation2.setId(2);
        vacation2.setStart(LocalDate.of(2021,1,2));
        vacation2.setEnd(LocalDate.of(2021,1,3));
        List<Vacation> vacations = Arrays.asList(vacation1, vacation2);
        List<VacationDto> vacationDtos = vacations.stream().map(vacationMapper::vacationToDto).collect(Collectors.toList());
        Page<VacationDto> pageDtos = new PageImpl<>(vacationDtos, PageRequest.of(0, 3), 2);

        mockMvc.perform(get("/api/teachers/1/vacations")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
            .andExpect(status().isOk());
    }

    @Test
    public void whenGetOneVacation_thenOneVacationReturned() throws Exception {
        Vacation vacation = createVacationNoId();
        vacation.setId(1);
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation);

        mockMvc.perform(get("/api/teachers/{teacherId}/vacations/{id}", vacation.getTeacher().getId(), vacation.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(vacationDto)))
            .andExpect(status().isOk());
    }

    @Test
    public void whenSaveVacation_thenVacationSaved() throws Exception {
        Vacation vacation = createVacationNoId();
        vacation.getTeacher().setId(2);
        vacation.getTeacher().setFirstName("FirstName1");
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation);

        mockMvc.perform(post("/api/teachers/{id}/vacations", vacation.getTeacher().getId())
            .content(objectMapper.writeValueAsString(vacationDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    public void whenEditVacation_thenVacationFound() throws Exception {
        Vacation vacation = createVacationNoId();
        vacation.setId(1);
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation);

        mockMvc.perform(patch("/api/teachers/{id}/vacations/{vacId}", vacation.getTeacher().getId(), 1)
            .content(objectMapper.writeValueAsString(vacationDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteVacation_thenVacationDeleted() throws Exception {
        mockMvc.perform(delete("/api/teachers/{id}/vacations/{vacId}", 1, 1)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void whenChangeTeacherOnLectures_thenTeachersFound() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience = Audience.builder().id(1).room(1).capacity(5).cathedra(cathedra).build();
        Group group = Group.builder().id(1).name("Killers").cathedra(cathedra).build();
        Subject subject = Subject.builder().id(1).name("Test Name").description("Desc").cathedra(cathedra).build();
        Teacher teacher = Teacher.builder()
            .id(1)
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
        LectureTime time = LectureTime.builder()
            .id(1)
            .start(LocalTime.of(8, 0))
            .end(LocalTime.of(9, 0))
            .build();
        Lecture lecture = Lecture.builder()
            .id(1)
            .audience(audience)
            .cathedra(cathedra)
            .date(LocalDate.of(2021, 1, 1))
            .groups(Arrays.asList(group))
            .subject(subject)
            .teacher(teacher)
            .time(time)
            .build();
        List<LectureDto> lectureDtos = Arrays.asList(lectureMapper.lectureToDto(lecture));

        mockMvc.perform(get("/api/teachers/{id}/vacations/lectures?start=2021-01-01&end=2021-01-02", teacher.getId())
            .contentType(APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(new Slice(lectureDtos))))
            .andExpect(status().isOk());
    }

    private Vacation createVacationNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Test Name").description("Desc").cathedra(cathedra).build();
        Teacher teacher = Teacher.builder()
            .id(1)
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

        return Vacation.builder()
            .teacher(teacher)
            .start(LocalDate.of(2021, 1, 1))
            .end(LocalDate.of(2021, 1, 2))
            .build();
    }
}
