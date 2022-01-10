package com.foxminded.university.dao;

import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class StudentRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private StudentRepository studentRepository;

    @Test
    void whenFindAll_thenAllExistingStudentsFound() {
        int expected = (int) (long) entityManager.createQuery("SELECT COUNT(s) FROM Student s").getSingleResult();
        List<Student> actual = studentRepository.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenPageable_whenFindPaginatedStudents_thenStudentsFound() {
        Group group = entityManager.find(Group.class, 1);
        List<Student> students = Arrays.asList(Student.builder()
            .firstName("Petr")
            .lastName("Orlov")
            .address("Empty Street 8")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1994, 3, 3))
            .phone("888005353535")
            .email("1@owl.com")
            .postalCode("999")
            .education("General secondary education")
            .group(group)
            .id(1)
            .build());
        Page<Student> actual = studentRepository.findAll(PageRequest.of(0, 1));
        Page<Student> expected = new PageImpl<>(students, PageRequest.of(0, 1), 5);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingStudent_whenFindById_thenStudentFound() {
        Group group = entityManager.find(Group.class, 1);
        Optional<Student> expected = Optional.of(Student.builder()
            .firstName("Petr")
            .lastName("Orlov")
            .address("Empty Street 8")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1994, 3, 3))
            .phone("888005353535")
            .email("1@owl.com")
            .postalCode("999")
            .education("General secondary education")
            .group(group)
            .id(1)
            .build());
        Optional<Student> actual = studentRepository.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingStudent_whenFindById_thenReturnEmptyOptional() {
        assertEquals(studentRepository.findById(100), Optional.empty());
    }

    @Test
    void givenNewStudent_whenSaveStudent_thenAllExistingStudentsFound() {
        Group group = entityManager.find(Group.class, 1);
        Student expected = Student.builder()
            .firstName("Petr123")
            .lastName("Orlov123")
            .address("Empty Street 8")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1994, 3, 3))
            .phone("88005353535")
            .email("1@owl.com")
            .postalCode("999")
            .education("General secondary education")
            .group(group)
            .build();
        studentRepository.save(expected);
        Student actual = entityManager.find(Student.class, 6);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingStudent_whenSaveWithChanges_thenChangesApplied() {
        Student expected = entityManager.find(Student.class, 1);
        expected.setFirstName("Test Name");
        studentRepository.save(expected);
        Student actual = entityManager.find(Student.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingStudent_thenStudentDeleted() {
        studentRepository.delete(Student.builder().id(2).build());
        Student actual = entityManager.find(Student.class, 2);

        assertNull(actual);
    }

    @Test
    void givenFirstNameAndLastNameAndBirthDate_whenFindByFullNameAndBirthDate_thenStudentFound() {
        Group group = entityManager.find(Group.class, 1);
        Optional<Student> expected = Optional.of(Student.builder()
            .firstName("Petr")
            .lastName("Orlov")
            .address("Empty Street 8")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1994, 3, 3))
            .phone("888005353535")
            .email("1@owl.com")
            .postalCode("999")
            .education("General secondary education")
            .group(group)
            .id(1)
            .build());
        Optional<Student> actual = studentRepository.findByFirstNameAndLastNameAndBirthDate(expected.get().getFirstName(),
            expected.get().getLastName(), expected.get().getBirthDate());

        assertEquals(expected, actual);
    }

    @Test
    void givenGroupName_whenFindByGroupId_thenStudentsFound() {
        Group group = entityManager.find(Group.class, 2);
        Student student1 = Student.builder()
            .firstName("Kim")
            .lastName("Cattrall")
            .address("Virtual Reality Capsule no 2")
            .gender(Gender.FEMALE)
            .birthDate(LocalDate.of(1956, 8, 21))
            .phone("312-555-0690:00")
            .email("4@owl.com")
            .postalCode("12345")
            .education("College education")
            .group(group)
            .id(4)
            .build();
        Student student2 = Student.builder()
            .firstName("Thomas")
            .lastName("Anderson")
            .address("Virtual Reality Capsule no 3")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1962, 3, 11))
            .phone("312-555-5555")
            .email("5@owl.com")
            .postalCode("12345")
            .education("College education")
            .group(group)
            .id(5)
            .build();
        List<Student> expected = Arrays.asList(student1, student2);
        List<Student> actual = studentRepository.findByGroupId(2);

        assertEquals(expected, actual);
    }
}
