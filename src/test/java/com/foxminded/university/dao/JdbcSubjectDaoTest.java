package com.foxminded.university.dao;

import com.foxminded.university.config.TestConfig;
import com.foxminded.university.dao.jdbc.JdbcSubjectDao;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcSubjectDaoTest {

    private final static String TABLE_NAME = "subjects";
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcSubjectDao subjectDao;

    @Test
    void whenFindAll_thenAllExistingSubjectsFound() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = subjectDao.findAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void givenPageable_whenFindPaginatedSubjects_thenSubjectsFound() {
        List<Subject> subjects = Arrays.asList(Subject.builder()
            .cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
            .name("Weapon Tactics")
            .description("Learning how to use heavy weapon and guerrilla tactics")
            .id(1)
            .build());
        Page<Subject> expected = new PageImpl<>(subjects, PageRequest.of(0, 1), 3);
        Page<Subject> actual = subjectDao.findPaginatedSubjects(PageRequest.of(0, 1));

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingSubject_whenFindById_thenSubjectFound() {
        Optional<Subject> expected = Optional.of(Subject.builder()
            .cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
            .name("Weapon Tactics")
            .description("Learning how to use heavy weapon and guerrilla tactics")
            .id(1)
            .build());
        Optional<Subject> actual = subjectDao.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingSubject_whenFindById_thenReturnEmptyOptional() {
        assertEquals(subjectDao.findById(100), Optional.empty());
    }

    @Test
    void givenNewSubject_whenSaveSubject_thenAllExistingSubjectsFound() {
        int expected = countRowsInTable(template, TABLE_NAME) + 1;
        subjectDao.save(Subject.builder()
            .cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
            .name("Weapon Tactics123")
            .description("Learning how to use heavy weapon and guerrilla tactics123")
            .build());

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void givenExitstingSubject_whenSaveWithChanges_thenChangesApplied() {
        Subject expected = Subject.builder()
            .id(1)
            .name("Test Name")
            .description("Test Description")
            .cathedra(Cathedra.builder().id(1).build())
            .build();
        subjectDao.save(expected);

        assertEquals(1, countRowsInTableWhere(template, TABLE_NAME,
            "id = 1 AND name = 'Test Name' AND description = 'Test Description'"));
    }

    @Test
    void whenDeleteExistingSubject_thenAllExistingSubjectsFound() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        subjectDao.deleteById(1);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void givenExistingSubject_whenFindByTeacherId_thenSubjectFound() {
        List<Subject> expected = new ArrayList<>();
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Subject subject1 = Subject.builder()
            .id(1).cathedra(cathedra)
            .name("Weapon Tactics")
            .description("Learning how to use heavy weapon and guerrilla tactics")
            .build();
        expected.add(subject1);
        List<Subject> actual = subjectDao.findByTeacherId(1);

        assertEquals(expected, actual);
    }

    @Test
    void givenSubjectName_whenFindByName_thenSubjectFound() {
        Optional<Subject> expected = Optional.of(Subject.builder()
            .cathedra(Cathedra.builder().id(1).name("Fantastic Cathedra").build())
            .name("Weapon Tactics")
            .description("Learning how to use heavy weapon and guerrilla tactics")
            .id(1)
            .build());
        Optional<Subject> actual = subjectDao.findByName("Weapon Tactics");

        assertEquals(expected, actual);
    }
}
