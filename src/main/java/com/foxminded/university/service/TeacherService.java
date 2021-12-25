package com.foxminded.university.service;

import com.foxminded.university.dao.TeacherRepository;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
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

    public void save(Teacher teacher) {
        logger.debug("Save teacher");
        uniqueCheck(teacher);
        teacherRepository.save(teacher);
    }

    public void delete(Teacher teacher) {
        logger.debug("Delete teacher with id: {}", teacher.getId());
        teacherRepository.delete(teacher);
    }

    private void uniqueCheck(Teacher teacher) {
        logger.debug("Check teacher is unique");
        Optional<Teacher> existingTeacher = teacherRepository.findByFullNameAndBirthDate(teacher.getFirstName(),
            teacher.getLastName(), teacher.getBirthDate());
        if (existingTeacher.isPresent() && (existingTeacher.get().getId() != teacher.getId())) {
            throw new EntityNotUniqueException("Teacher with full name " + teacher.getFirstName() + " "
                + teacher.getLastName() + " and birth date " + teacher.getBirthDate() + " is already exists!");
        }
    }

    public List<Teacher> findTeachersForChange(Lecture lecture) {
        logger.debug("Find teachers who havent lectures and vacation this periodand who can teach this subject");
        return teacherRepository.findByFreeDateAndSubjectWithCurrentTeacher(lecture.getDate(), lecture.getTime(),
            lecture.getSubject());
    }
}
