package com.foxminded.university.dao;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class SubjectRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    void whenFindAll_thenAllExistingSubjectsFound() {
        int expected = (int) (long) entityManager.createQuery("SELECT COUNT(s) FROM Subject s").getSingleResult();
        List<Subject> actual = subjectRepository.findAll();

        assertEquals(actual.size(), expected);
    }

    @Test
    void givenPageable_whenFindPaginatedSubjects_thenSubjectsFound() {
        List<Subject> subjects = Arrays.asList(Subject.builder()
            .cathedra(entityManager.find(Cathedra.class, 1))
            .name("Weapon Tactics")
            .description("Learning how to use heavy weapon and guerrilla tactics")
            .id(1)
            .build());
        Page<Subject> expected = new PageImpl<>(subjects, PageRequest.of(0, 1), 3);
        Page<Subject> actual = subjectRepository.findAll(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingSubject_whenFindById_thenSubjectFound() {
        Optional<Subject> expected = Optional.of(Subject.builder()
            .cathedra(entityManager.find(Cathedra.class, 1))
            .name("Weapon Tactics")
            .description("Learning how to use heavy weapon and guerrilla tactics")
            .id(1)
            .build());
        Optional<Subject> actual = subjectRepository.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingSubject_whenFindById_thenReturnEmptyOptional() {
        assertEquals(subjectRepository.findById(100), Optional.empty());
    }

    @Test
    void givenNewSubject_whenSaveSubject_thenAllExistingSubjectsFound() {
        Subject expected = Subject.builder()
            .cathedra(entityManager.find(Cathedra.class, 1))
            .name("Weapon Tactics123")
            .description("Learning how to use heavy weapon and guerrilla tactics123")
            .build();
        subjectRepository.save(expected);
        Subject actual = entityManager.find(Subject.class, 4);

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingSubject_whenSaveWithChanges_thenChangesApplied() {
        Subject expected = entityManager.find(Subject.class, 1);
        expected.setName("Test Name");
        subjectRepository.save(expected);
        Subject actual = entityManager.find(Subject.class, 1);

        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteExistingSubject_thenSubjectDeleted() {
        subjectRepository.delete(Subject.builder().id(2).build());
        Subject actual = entityManager.find(Subject.class, 2);

        assertNull(actual);
    }

    @Test
    void givenSubjectName_whenFindByName_thenSubjectFound() {
        Optional<Subject> expected = Optional.of(Subject.builder()
            .cathedra(entityManager.find(Cathedra.class, 1))
            .name("Weapon Tactics")
            .description("Learning how to use heavy weapon and guerrilla tactics")
            .id(1)
            .build());
        Optional<Subject> actual = subjectRepository.findByName("Weapon Tactics");

        assertEquals(expected, actual);
    }
}
