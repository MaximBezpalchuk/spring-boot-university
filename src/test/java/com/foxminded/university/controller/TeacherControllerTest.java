package com.foxminded.university.controller;

import com.foxminded.university.dao.jdbc.mapper.LectureToEventMapper;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    private MockMvc mockMvc;
    private final LectureToEventMapper lectureToEventMapper = LectureToEventMapper.INSTANCE;
    @Mock
    private TeacherService teacherService;
    @Mock
    private SubjectService subjectService;
    @Mock
    private CathedraService cathedraService;
    @Mock
    private LectureService lectureService;
    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 2));
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).setCustomArgumentResolvers(resolver).build();
    }

    @Test
    public void whenGetAllTeachers_thenAllTeachersReturned() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Subject name").build();
        Teacher teacher1 = Teacher.builder()
            .id(1)
            .firstName("Name")
            .lastName("Last name")
            .cathedra(cathedra)
            .subjects(Arrays.asList(subject))
            .build();
        Teacher teacher2 = Teacher.builder()
            .id(2)
            .firstName("Name2")
            .lastName("Last name")
            .cathedra(cathedra)
            .subjects(Arrays.asList(subject))
            .build();
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        Page<Teacher> page = new PageImpl<>(teachers, PageRequest.of(0, 2), 1);
        when(teacherService.findAll(isA(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/teachers"))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/index"))
            .andExpect(forwardedUrl("teachers/index"))
            .andExpect(model().attribute("teachers", page));
        verifyNoMoreInteractions(teacherService);
    }

    @Test
    public void whenGetOneTeacher_thenOneTeacherReturned() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Subject name").build();
        Teacher teacher = Teacher.builder()
            .id(1)
            .firstName("Name")
            .lastName("Last name")
            .cathedra(cathedra)
            .subjects(Arrays.asList(subject))
            .build();
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        mockMvc.perform(get("/teachers/{id}", teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/show"))
            .andExpect(forwardedUrl("teachers/show"))
            .andExpect(model().attribute("teacher", teacher));
    }

    @Test
    void whenCreateNewTeacher_thenNewTeacherCreated() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Subject name").build();

        when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
        when(subjectService.findAll()).thenReturn(Arrays.asList(subject));

        mockMvc.perform(get("/teachers/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/new"))
            .andExpect(forwardedUrl("teachers/new"))
            .andExpect(model().attribute("teacher", instanceOf(Teacher.class)));
    }

    @Test
    void whenSaveTeacher_thenTeacherSaved() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Subject name").build();
        Teacher teacher = Teacher.builder()
            .firstName("Name")
            .lastName("Last name")
            .cathedra(cathedra)
            .subjects(Arrays.asList(subject))
            .build();

        mockMvc.perform(post("/teachers").flashAttr("teacher", teacher))
            .andExpect(redirectedUrl("/teachers"));

        verify(teacherService).save(teacher);
    }

    @Test
    void whenEditTeacher_thenTeacherFound() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject = Subject.builder().id(1).name("Subject name").build();
        Teacher expected = Teacher.builder()
            .id(1)
            .firstName("Name")
            .lastName("Last name")
            .cathedra(cathedra)
            .subjects(Arrays.asList(subject))
            .build();

        when(teacherService.findById(1)).thenReturn(expected);
        when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
        when(subjectService.findAll()).thenReturn(Arrays.asList(subject));

        mockMvc.perform(get("/teachers/{id}/edit", 1))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/edit"))
            .andExpect(forwardedUrl("teachers/edit"))
            .andExpect(model().attribute("teacher", is(expected)));
    }

    @Test
    void whenDeleteTeacher_thenTeacherDeleted() throws Exception {
        mockMvc.perform(delete("/teachers/{id}", 1))
            .andExpect(redirectedUrl("/teachers"));

        verify(teacherService).deleteById(1);
    }
}