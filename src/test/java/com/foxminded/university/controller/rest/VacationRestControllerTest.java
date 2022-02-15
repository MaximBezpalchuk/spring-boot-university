package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.dto.VacationDto;
import com.foxminded.university.model.*;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.VacationService;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class VacationRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private GroupMapper groupMapper = new GroupMapperImpl(cathedraMapper);
    private AudienceMapper audienceMapper = new AudienceMapperImpl(cathedraMapper);
    private LectureTimeMapper lectureTimeMapper = Mappers.getMapper(LectureTimeMapper.class);
    private SubjectMapper subjectMapper = new SubjectMapperImpl(cathedraMapper);
    private TeacherMapper teacherMapper = new TeacherMapperImpl(cathedraMapper, subjectMapper);
    @Spy
    private VacationMapper vacationMapper = new VacationMapperImpl(teacherMapper);
    @Spy
    private LectureMapper lectureMapper = new LectureMapperImpl(cathedraMapper, groupMapper, teacherMapper, audienceMapper, subjectMapper, lectureTimeMapper);
    @Mock
    private LectureService lectureService;
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
    }

    @Test
    public void whenGetAllVacations_thenAllVacationsReturned() throws Exception {
        Vacation vacation1 = createVacationNoId();
        vacation1.setId(1);
        Vacation vacation2 = createVacationNoId();
        vacation2.setId(2);
        List<Vacation> vacations = Arrays.asList(vacation1, vacation2);
        Page<Vacation> page = new PageImpl<>(vacations, PageRequest.of(0, 1), 2);
        List<VacationDto> vacationDtos = vacations.stream().map(vacationMapper::vacationToDto).collect(Collectors.toList());
        Page<VacationDto> pageDtos = new PageImpl<>(vacationDtos, PageRequest.of(0, 1), 2);
        when(vacationService.findByTeacherId(PageRequest.of(0, 1), vacation1.getTeacher().getId())).thenReturn(page);

        mockMvc.perform(get("/api/teachers/{id}/vacations", vacation1.getTeacher().getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(vacationService);
    }

    @Test
    public void whenGetOneVacation_thenOneVacationReturned() throws Exception {
        Vacation vacation = createVacationNoId();
        vacation.setId(1);
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation);
        when(vacationService.findByTeacherIdAndId(1, vacation.getId())).thenReturn(vacation);

        mockMvc.perform(get("/api/teachers/{teacherId}/vacations/{id}", vacation.getTeacher().getId(), vacation.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(vacationDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveVacation_thenVacationSaved() throws Exception {
        Vacation vacation = createVacationNoId();
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation);
        when(lectureService.findByTeacherIdAndPeriod(vacation.getTeacher().getId(), vacation.getStart(), vacation.getEnd())).thenReturn(new ArrayList<>());
        when(vacationService.save(vacation)).thenAnswer(I -> {
            vacation.setId(2);
            return vacation;
        });

        mockMvc.perform(post("/api/teachers/{id}/vacations", vacation.getTeacher().getId())
                .content(objectMapper.writeValueAsString(vacationDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/teachers/1/vacations/2"))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenEditVacation_thenVacationFound() throws Exception {
        Vacation vacation = createVacationNoId();
        vacation.setId(1);
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation);
        when(lectureService.findByTeacherIdAndPeriod(vacation.getTeacher().getId(), vacation.getStart(), vacation.getEnd())).thenReturn(new ArrayList<>());
        when(vacationService.save(vacation)).thenReturn(vacation);

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

        verify(vacationService).delete(1);
    }

    @Test
    public void whenChangeTeacherOnLectures_thenTeachersFound() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT).gender(Gender.MALE)
                .build();
        List<Lecture> lectures = Arrays.asList(Lecture.builder().id(1).teacher(teacher).build());
        List<LectureDto> lectureDtos = lectures.stream().map(lectureMapper::lectureToDto).collect(Collectors.toList());

        when(lectureService.findByTeacherIdAndPeriod(1, LocalDate.of(2021, 4, 4), LocalDate.of(2021, 4, 5)))
                .thenReturn(lectures);

        mockMvc.perform(get("/api/teachers/{id}/vacations/lectures?start=2021-04-04&end=2021-04-05", teacher.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(new Slice(lectureDtos))))
                .andExpect(status().isOk());
    }

    private Vacation createVacationNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).cathedra(cathedra).name("Subject name").description("Subject desc")
                .build();
        Teacher teacher = Teacher.builder().id(1).firstName("TestName").lastName("TestLastName").phone("88005553535")
                .address("Address").email("one@mail.com").gender(Gender.MALE).postalCode("123").education("Edu")
                .birthDate(LocalDate.of(2020, 1, 1)).cathedra(cathedra).subjects(Arrays.asList(subject))
                .degree(Degree.PROFESSOR).build();

        return Vacation.builder()
                .teacher(teacher)
                .start(LocalDate.of(2021, 1, 1))
                .end(LocalDate.of(2021, 1, 2))
                .build();
    }
}