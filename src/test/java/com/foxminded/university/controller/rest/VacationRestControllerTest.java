package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.LectureMapper;
import com.foxminded.university.dao.mapper.TeacherMapper;
import com.foxminded.university.dao.mapper.VacationMapper;
import com.foxminded.university.dto.ObjectListDto;
import com.foxminded.university.dto.VacationDto;
import com.foxminded.university.model.*;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class VacationRestControllerTest {

    private MockMvc mockMvc;
    private final VacationMapper vacationMapper = Mappers.getMapper(VacationMapper.class);
    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);
    private final LectureMapper lectureMapper = Mappers.getMapper(LectureMapper.class);
    private ObjectMapper objectMapper;
    @Mock
    private LectureService lectureService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private VacationService vacationService;
    @InjectMocks
    private VacationRestController vacationRestController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(vacationRestController).setCustomArgumentResolvers(resolver).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ReflectionTestUtils.setField(vacationRestController, "vacationMapper", vacationMapper);
        ReflectionTestUtils.setField(vacationRestController, "lectureMapper", lectureMapper);
        ReflectionTestUtils.setField(vacationMapper, "teacherService", teacherService);
        ReflectionTestUtils.setField(vacationMapper, "teacherMapper", teacherMapper);
        ReflectionTestUtils.setField(lectureMapper, "teacherMapper", teacherMapper);
    }

    @Test
    public void whenGetAllVacations_thenAllVacationsReturned() throws Exception {
        Vacation vacation1 = createVacationNoId();
        vacation1.setId(1);
        Vacation vacation2 = createVacationNoId();
        vacation2.setId(2);
        List<Vacation> vacations = Arrays.asList(vacation1, vacation2);
        Page<Vacation> page = new PageImpl<>(vacations, PageRequest.of(0, 1), 2);
        when(vacationService.findByTeacherId(PageRequest.of(0, 1), vacation1.getTeacher().getId())).thenReturn(page);

        mockMvc.perform(get("/api/teachers/{id}/vacations", vacation1.getTeacher().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ObjectListDto(vacations))))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(vacationService);
    }

    @Test
    public void whenGetOneVacation_thenOneVacationReturned() throws Exception {
        Vacation vacation = createVacationNoId();
        vacation.setId(1);
        when(vacationService.findById(vacation.getId())).thenReturn(vacation);

        mockMvc.perform(get("/api/teachers/{teacherId}/vacations/{id}", vacation.getTeacher().getId(), vacation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vacationMapper.vacationToDto(vacation))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveVacation_thenVacationSaved() throws Exception {
        Vacation vacation1 = createVacationNoId();
        Vacation vacation2 = createVacationNoId();
        vacation2.setId(2);
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation1);
        when(teacherService.findByFirstNameAndLastNameAndBirthDate(vacationDto.getTeacherDto().getFirstName(), vacationDto.getTeacherDto().getLastName(), vacationDto.getTeacherDto().getBirthDate())).thenReturn(vacation1.getTeacher());
        when(lectureService.findByTeacherIdAndPeriod(vacation1.getTeacher().getId(), vacation1.getStart(), vacation1.getEnd())).thenReturn(new ArrayList<>());
        when(vacationService.save(vacation1)).thenReturn(vacation2);

        mockMvc.perform(post("/api/teachers/{id}/vacations", vacation1.getTeacher().getId())
                .content(objectMapper.writeValueAsString(vacationDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/teachers/1/vacations/2"))
                .andExpect(status().isCreated());

        verify(vacationService).save(vacation1);
    }

    @Test
    public void whenEditVacation_thenVacationFound() throws Exception {
        Vacation vacation = createVacationNoId();
        vacation.setId(1);
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation);
        when(teacherService.findByFirstNameAndLastNameAndBirthDate(vacationDto.getTeacherDto().getFirstName(), vacationDto.getTeacherDto().getLastName(), vacationDto.getTeacherDto().getBirthDate())).thenReturn(vacation.getTeacher());
        when(lectureService.findByTeacherIdAndPeriod(vacation.getTeacher().getId(), vacation.getStart(), vacation.getEnd())).thenReturn(new ArrayList<>());
        when(vacationService.save(vacation)).thenReturn(vacation);

        mockMvc.perform(patch("/api/teachers/{id}/vacations/{vacId}", vacation.getTeacher().getId(), 1)
                .content(objectMapper.writeValueAsString(vacationDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteVacation_thenVacationDeleted() throws Exception {
        Vacation vacation = createVacationNoId();
        vacation.setId(1);
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation);
        when(teacherService.findByFirstNameAndLastNameAndBirthDate(vacationDto.getTeacherDto().getFirstName(), vacationDto.getTeacherDto().getLastName(), vacationDto.getTeacherDto().getBirthDate())).thenReturn(vacation.getTeacher());

        mockMvc.perform(delete("/api/teachers/{id}/vacations/{vacId}", 1, 1)
                .content(objectMapper.writeValueAsString(vacationDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(vacationService).delete(vacation);
    }

    @Test
    public void whenChangeTeacherOnLectures_thenTeachersFound() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).gender(Gender.MALE)
                .build();
        Lecture lecture = Lecture.builder().id(1).teacher(teacher).build();

        when(lectureService.findByTeacherIdAndPeriod(1, LocalDate.of(2021, 4, 4), LocalDate.of(2021, 4, 5)))
                .thenReturn(Arrays.asList(lecture));

        mockMvc.perform(get("/api/teachers/{id}/vacations/lectures?start=2021-04-04&end=2021-04-05", teacher.getId())
                .content(objectMapper.writeValueAsString(new ObjectListDto(Arrays.asList(lectureMapper.lectureToDto(lecture)))))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private Vacation createVacationNoId() {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).gender(Gender.MALE)
                .build();

        return Vacation.builder()
                .teacher(teacher)
                .start(LocalDate.of(2021, 1, 1))
                .end(LocalDate.of(2021, 1, 2))
                .build();
    }
}