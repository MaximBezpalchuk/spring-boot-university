package com.foxminded.university.service;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.GroupOverflowException;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentDao studentDao;
    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(studentService, "maxGroupSize", 1);
    }

    @Test
    void givenListOfStudents_whenFindAll_thenAllExistingStudentsFound() {
        Student student1 = Student.builder().id(1).build();
        List<Student> expected = Arrays.asList(student1);
        when(studentDao.findAll()).thenReturn(expected);
        List<Student> actual = studentService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenPageable_whenFindAll_thenAllPageableStudentsFound() {
        List<Student> students = Arrays.asList(Student.builder().id(1).build());
        Page<Student> expected = new PageImpl<>(students, PageRequest.of(0, 1), 1);
        when(studentDao.findPaginatedStudents(isA(Pageable.class))).thenReturn(expected);
        Page<Student> actual = studentService.findAll(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingStudent_whenFindById_thenStudentFound() {
        Optional<Student> expected = Optional.of(Student.builder().id(1).build());
        when(studentDao.findById(1)).thenReturn(expected);
        Student actual = studentService.findById(1);

        assertEquals(expected.get(), actual);
    }

    @Test
    void givenExistingStudent_whenFindById_thenEntityNotFoundException() {
        when(studentDao.findById(10)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            studentService.findById(10);
        });

        assertEquals("Can`t find any student with id: 10", exception.getMessage());
    }

    @Test
    void givenNewStudent_whenSave_thenSaved() {
        Student student = Student.builder().id(1).group(Group.builder().name("Test").build()).build();
        when(studentDao.findByFullNameAndBirthDate(student.getFirstName(), student.getLastName(),
            student.getBirthDate())).thenReturn(Optional.of(student));
        when(studentDao.findByGroupId(student.getGroup().getId())).thenReturn(new ArrayList<>());
        studentService.save(student);

        verify(studentDao).save(student);
    }

    @Test
    void givenExistingStudent_whenSave_thenSaved() {
        Student student = Student.builder()
            .id(1)
            .firstName("TestFirstName")
            .lastName("TestLastName")
            .group(Group.builder().id(10).name("Test").build())
            .build();
        when(studentDao.findByFullNameAndBirthDate(student.getFirstName(), student.getLastName(),
            student.getBirthDate())).thenReturn(Optional.of(student));
        when(studentDao.findByGroupId(student.getGroup().getId())).thenReturn(new ArrayList<>());
        studentService.save(student);

        verify(studentDao).save(student);
    }

    @Test
    void givenNotUniqueStudent_whenSave_thenEntityNotUniqueException() {
        Student student1 = Student.builder()
            .id(1)
            .firstName("TestFirstName")
            .lastName("TestLastName")
            .birthDate(LocalDate.of(2020, 1, 1))
            .group(Group.builder().id(10).name("Test").build())
            .build();
        Student student2 = Student.builder()
            .id(11)
            .firstName("TestFirstName")
            .lastName("TestLastName")
            .birthDate(LocalDate.of(2020, 1, 1))
            .group(Group.builder().id(10).name("Test").build())
            .build();
        when(studentDao.findByFullNameAndBirthDate(student1.getFirstName(),
            student1.getLastName(), student1.getBirthDate())).thenReturn(Optional.of(student2));
        Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
            studentService.save(student1);
        });

        assertEquals("Student with full name TestFirstName TestLastName and  birth date 2020-01-01 is already exists!",
            exception.getMessage());
    }

    @Test
    void givenStudentWhenGroupIsFull_whenSave_thenStudentGroupIsFullException() {
        Student student = Student.builder()
            .id(1)
            .firstName("TestFirstName")
            .lastName("TestLastName")
            .group(Group.builder().id(10).name("Test").build())
            .build();
        when(studentDao.findByFullNameAndBirthDate(student.getFirstName(), student.getLastName(),
            student.getBirthDate())).thenReturn(Optional.of(student));
        when(studentDao.findByGroupId(student.getGroup().getId())).thenReturn(Arrays.asList(student, student));
        Exception exception = assertThrows(GroupOverflowException.class, () -> {
            studentService.save(student);
        });

        assertEquals("This group is already full! Group size is: 2. Max group size is: 1", exception.getMessage());
    }

    @Test
    void givenExistingStudentId_whenDelete_thenDeleted() {
        Student student = Student.builder().id(1).build();
        studentService.delete(student);

        verify(studentDao).delete(student);
    }
}
