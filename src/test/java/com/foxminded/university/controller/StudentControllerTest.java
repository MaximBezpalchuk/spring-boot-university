package com.foxminded.university.controller;

import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.StudentService;
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

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    private MockMvc mockMvc;
    private Validator validator;
    @Mock
    private StudentService studentService;
    @Mock
    private GroupService groupService;
    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).setCustomArgumentResolvers(resolver).build();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenGetAllStudents_thenAllStudentsReturned() throws Exception {
        Group group = Group.builder().id(1).name("Killers").build();
        Student student1 = Student.builder()
                .id(1)
                .firstName("Name")
                .lastName("Last name")
                .group(group)
                .build();
        Student student2 = Student.builder()
                .id(2)
                .firstName("Name")
                .lastName("Last name")
                .group(group)
                .build();
        List<Student> students = Arrays.asList(student1, student2);
        Page<Student> page = new PageImpl<>(students, PageRequest.of(0, 1), 2);
        when(studentService.findAll(isA(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/index"))
                .andExpect(forwardedUrl("students/index"))
                .andExpect(model().attribute("students", page));
        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void whenGetOneStudent_thenOneStudentReturned() throws Exception {
        Group group = Group.builder().id(1).name("Killers").build();
        Student student = Student.builder()
                .id(1)
                .firstName("Name")
                .lastName("Last name")
                .group(group)
                .build();
        when(studentService.findById(student.getId())).thenReturn(student);

        mockMvc.perform(get("/students/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("students/show"))
                .andExpect(forwardedUrl("students/show"))
                .andExpect(model().attribute("student", student));
    }

    @Test
    void whenCreateNewStudent_thenNewStudentCreated() throws Exception {
        Group group = Group.builder().id(1).name("Killers").build();

        when(groupService.findAll()).thenReturn(Arrays.asList(group));

        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/new"))
                .andExpect(forwardedUrl("students/new"))
                .andExpect(model().attribute("student", instanceOf(Student.class)));
    }

    @Test
    void whenSaveStudent_thenStudentSaved() throws Exception {
        Group group = Group.builder().id(1).name("Killers").build();
        Student student = Student.builder()
                .firstName("Name")
                .lastName("Last name")
                .group(group)
                .build();
        mockMvc.perform(post("/students").flashAttr("student", student))
                .andExpect(redirectedUrl("/students"));

        verify(studentService).save(student);
    }

    @Test
    void whenEditStudent_thenStudentFound() throws Exception {
        Group group = Group.builder().id(1).name("Killers").build();
        Student expected = Student.builder()
                .id(1)
                .firstName("Name")
                .lastName("Last name")
                .group(group)
                .build();

        when(studentService.findById(1)).thenReturn(expected);
        when(groupService.findAll()).thenReturn(Arrays.asList(group));

        mockMvc.perform(get("/students/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("students/edit"))
                .andExpect(forwardedUrl("students/edit"))
                .andExpect(model().attribute("student", is(expected)));
    }

    @Test
    void whenDeleteStudent_thenStudentDeleted() throws Exception {
        mockMvc.perform(delete("/students/{id}", 1))
                .andExpect(redirectedUrl("/students"));

        verify(studentService).delete(Student.builder().id(1).build());
    }

    @Test
    void whenGivenIncorrectPhoneStudent_thenValidationPhoneNumberFailed() {
        Student student = Student.builder()
                .firstName("Petr123")
                .lastName("Orlov123")
                .address("Empty Street 8")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1994, 3, 3))
                .phone("88005353535123321123123123123")
                .email("1@owl.com")
                .postalCode("999")
                .education("General secondary education")
                .build();
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertFalse(violations.isEmpty());
    }

    @Test
    void whenGivenIncorrectAgeStudent_thenValidationMinAgeFailed() {
        Student student = Student.builder()
                .firstName("Petr123")
                .lastName("Orlov123")
                .address("Empty Street 8")
                .gender(Gender.MALE)
                .birthDate(LocalDate.now().minus(13, ChronoUnit.YEARS))
                .phone("8800")
                .email("1@owl.com")
                .postalCode("999")
                .education("General secondary education")
                .build();
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertFalse(violations.isEmpty());
    }
}