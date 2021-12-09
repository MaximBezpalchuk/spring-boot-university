package com.foxminded.university.controller;

import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.TeacherService;
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class VacationControllerTest {

    private MockMvc mockMvc;
    @Mock
    private VacationService vacationService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private LectureService lectureService;
    @InjectMocks
    private VacationController vacationController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(vacationController).setCustomArgumentResolvers(resolver).build();
    }

    @Test
    public void whenGetAllVacations_thenAllVacationsReturned() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT)
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
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(vacationService.findByTeacherId(PageRequest.of(0, 1), teacher.getId())).thenReturn(page);

        mockMvc.perform(get("/teachers/{id}/vacations", teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/index"))
                .andExpect(forwardedUrl("teachers/vacations/index"))
                .andExpect(model().attribute("vacations", page));
        verifyNoMoreInteractions(vacationService);
    }

    @Test
    public void whenGetOneVacation_thenOneVacationReturned() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT)
                .build();
        Vacation vacation = Vacation.builder()
                .id(1)
                .teacher(teacher)
                .start(LocalDate.of(2021, 1, 1))
                .end(LocalDate.of(2021, 1, 2))
                .build();
        when(vacationService.findById(vacation.getId())).thenReturn(vacation);

        mockMvc.perform(get("/teachers/{teacherId}/vacations/{id}", teacher.getId(), vacation.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/show"))
                .andExpect(forwardedUrl("teachers/vacations/show"))
                .andExpect(model().attribute("vacation", vacation));
    }

    @Test
    void whenCreateNewVacation_thenNewVacationCreated() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT)
                .build();

        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        mockMvc.perform(get("/teachers/{id}/vacations/new", teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/new"))
                .andExpect(forwardedUrl("teachers/vacations/new"))
                .andExpect(model().attribute("vacation", instanceOf(Vacation.class)));
    }

    @Test
    void whenSaveVacation_thenVacationSaved() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT)
                .build();
        Vacation vacation = Vacation.builder()
                .teacher(teacher)
                .start(LocalDate.of(2021, 1, 1))
                .end(LocalDate.of(2021, 1, 2))
                .build();
        mockMvc.perform(post("/teachers/{id}/vacations", teacher.getId()).flashAttr("vacation", vacation))
                .andExpect(redirectedUrl("/teachers/" + teacher.getId() + "/vacations"));

        verify(vacationService).save(vacation);
    }

    @Test
    void whenEditVacation_thenVacationFound() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT)
                .build();
        Vacation expected = Vacation.builder()
                .id(1)
                .teacher(teacher)
                .start(LocalDate.of(2021, 1, 1))
                .end(LocalDate.of(2021, 1, 2))
                .build();

        when(vacationService.findById(1)).thenReturn(expected);
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        mockMvc.perform(get("/teachers/{id}/vacations/{vacId}/edit", teacher.getId(), 1))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/edit"))
                .andExpect(forwardedUrl("teachers/vacations/edit"))
                .andExpect(model().attribute("vacation", is(expected)));
    }

    @Test
    void whenDeleteVacation_thenVacationDeleted() throws Exception {
        mockMvc.perform(delete("/teachers/{id}/vacations/{vacId}", 1, 1))
                .andExpect(redirectedUrl("/teachers/1/vacations"));

        verify(vacationService).delete(Vacation.builder().id(1).build());
    }

    @Test
    void whenChangeTeacherOnLectures_thenTeachersFound() throws Exception {
        Teacher teacher = Teacher.builder().id(1).firstName("Name").lastName("Last name").degree(Degree.ASSISTANT)
                .build();
        Lecture lecture = Lecture.builder().id(1).teacher(teacher).build();
        when(lectureService.findByTeacherIdAndPeriod(teacher.getId(), LocalDate.of(2021, 04, 04),
                LocalDate.of(2021, 04, 05))).thenReturn(Arrays.asList(lecture));
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        mockMvc.perform(get("/teachers/{id}/vacations/lectures?start=2021-04-04&end=2021-04-05", teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/lectures"))
                .andExpect(forwardedUrl("teachers/vacations/lectures"))
                .andExpect(model().attribute("teacher", is(teacher)))
                .andExpect(model().attribute("lectures", is(Arrays.asList(lecture))))
                .andExpect(model().attribute("start", is(LocalDate.of(2021, 4, 4))))
                .andExpect(model().attribute("end", is(LocalDate.of(2021, 4, 5))));
    }
}