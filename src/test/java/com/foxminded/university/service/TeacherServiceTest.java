package com.foxminded.university.service;

import com.foxminded.university.dao.TeacherRepository;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;
    @InjectMocks
    private TeacherService teacherService;

    @Test
    void givenListOfTeachers_whenFindAll_thenAllExistingTeachersFound() {
        Teacher teacher1 = Teacher.builder().id(1).build();
        List<Teacher> expected = Arrays.asList(teacher1);
        when(teacherRepository.findAll()).thenReturn(expected);
        List<Teacher> actual = teacherService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenPageable_whenFindAll_thenAllPageableTeachersFound() {
        List<Teacher> teachers = Arrays.asList(Teacher.builder().id(1).build());
        Page<Teacher> expected = new PageImpl<>(teachers, PageRequest.of(0, 1), 1);
        when(teacherRepository.findAll(PageRequest.of(0, 1))).thenReturn(expected);
        Page<Teacher> actual = teacherService.findAll(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingTeacher_whenFindById_thenTeacherFound() {
        Optional<Teacher> expected = Optional.of(Teacher.builder().id(1).build());
        when(teacherRepository.findById(1)).thenReturn(expected);
        Teacher actual = teacherService.findById(1);

        assertEquals(expected.get(), actual);
    }

    @Test
    void givenExistingTeacher_whenFindById_thenEntityNotFoundException() {
        when(teacherRepository.findById(10)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            teacherService.findById(10);
        });

        assertEquals("Can`t find any teacher with id: 10", exception.getMessage());
    }

    @Test
    void givenNewTeacher_whenSave_thenSaved() {
        Teacher teacher = Teacher.builder().id(1).build();
        teacherService.save(teacher);

        verify(teacherRepository).save(teacher);
    }

    @Test
    void givenExistingTeacher_whenSave_thenSaved() {
        Teacher teacher = Teacher.builder().id(1)
            .firstName("TestFirstName")
            .lastName("TestLastName")
            .birthDate(LocalDate.of(1920, 2, 12))
            .build();
        when(teacherRepository.findByFirstNameAndLastNameAndBirthDate(teacher.getFirstName(), teacher.getLastName(),
            teacher.getBirthDate())).thenReturn(Optional.of(teacher));
        teacherService.save(teacher);

        verify(teacherRepository).save(teacher);
    }

    @Test
    void givenExistingTeacherId_whenDelete_thenDeleted() {
        Teacher teacher = Teacher.builder().id(1).build();
        teacherService.delete(teacher);

        verify(teacherRepository).delete(teacher);
    }

    @Test
    void givenNotUniqueTeacher_whenSave_thenEntityNotUniqueException() {
        Teacher teacher1 = Teacher.builder().id(1)
            .firstName("TestFirstName")
            .lastName("TestLastName")
            .birthDate(LocalDate.of(1920, 2, 12))
            .build();
        Teacher teacher2 = Teacher.builder().id(10)
            .firstName("TestFirstName")
            .lastName("TestLastName")
            .birthDate(LocalDate.of(1920, 2, 12))
            .build();
        when(teacherRepository.findByFirstNameAndLastNameAndBirthDate(teacher1.getFirstName(), teacher1.getLastName(),
            teacher1.getBirthDate())).thenReturn(Optional.of(teacher2));
        Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
            teacherService.save(teacher1);
        });

        assertEquals("Teacher with full name TestFirstName TestLastName and birth date 1920-02-12 is already exists!",
            exception.getMessage());
    }
}
