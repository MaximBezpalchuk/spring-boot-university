package com.foxminded.university.service;

import com.foxminded.university.dao.LectureRepository;
import com.foxminded.university.dao.TeacherRepository;
import com.foxminded.university.dao.VacationRepository;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    private final TeacherRepository teacherRepository;
    private final LectureRepository lectureRepository;
    private final VacationRepository vacationRepository;

    public TeacherService(TeacherRepository teacherRepository, LectureRepository lectureRepository, VacationRepository vacationRepository) {
        this.teacherRepository = teacherRepository;
        this.lectureRepository = lectureRepository;
        this.vacationRepository = vacationRepository;
    }

    public List<Teacher> findAll() {
        logger.debug("Find all teachers");
        return teacherRepository.findAll();
    }

    public Page<Teacher> findAll(final Pageable pageable) {
        logger.debug("Find all teachers paginated");
        return teacherRepository.findAll(pageable);
    }

    public Teacher findById(int id) {
        logger.debug("Find teacher by id {}", id);
        return teacherRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any teacher with id: " + id));
    }

    public Teacher findByFirstNameAndLastNameAndBirthDate(String firstName, String lastName, LocalDate birthDate) {
        logger.debug("Find teacher by firstName: {}, lastName: {} and birthDate: {}", firstName, lastName, birthDate);
        return teacherRepository.findByFirstNameAndLastNameAndBirthDate(firstName, lastName, birthDate)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any teacher with firstName: " + firstName + ", lastName: " + lastName + " and birthDate: " + birthDate));
    }

    public Teacher save(Teacher teacher) {
        logger.debug("Save teacher");
        uniqueCheck(teacher);

        return teacherRepository.save(teacher);
    }

    public void delete(int id) {
        logger.debug("Delete teacher with id: {}", id);
        teacherRepository.deleteById(id);
    }

    private void uniqueCheck(Teacher teacher) {
        logger.debug("Check teacher is unique");
        Optional<Teacher> existingTeacher = teacherRepository.findByFirstNameAndLastNameAndBirthDate(teacher.getFirstName(),
            teacher.getLastName(), teacher.getBirthDate());
        if (existingTeacher.isPresent() && (existingTeacher.get().getId() != teacher.getId())) {
            throw new EntityNotUniqueException("Teacher with full name " + teacher.getFirstName() + " "
                + teacher.getLastName() + " and birth date " + teacher.getBirthDate() + " is already exists!");
        }
    }

    public List<Teacher> findTeachersForChange(Lecture lecture) {
        logger.debug("Find teachers who havent lectures and vacation this periodand who can teach this subject");
        return teacherRepository.findBySubjectsContaining(lecture.getSubject()).stream()
            .filter(teacher -> lectureRepository.findLecturesByTeacherAndDateAndTime(teacher, lecture.getDate(), lecture.getTime()).isEmpty())
            .filter(teacher -> vacationRepository.findByTeacherAndStartGreaterThanEqualAndEndLessThanEqual(teacher, lecture.getDate(), lecture.getDate()).isEmpty())
            .collect(Collectors.toList());
    }
}
