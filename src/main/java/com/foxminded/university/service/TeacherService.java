package com.foxminded.university.service;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
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

    private final TeacherDao teacherDao;

    public TeacherService(JdbcTeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    public List<Teacher> findAll() {
        logger.debug("Find all teachers");
        return teacherDao.findAll();
    }

    public Page<Teacher> findAll(final Pageable pageable) {
        logger.debug("Find all teachers paginated");
        return teacherDao.findPaginatedTeachers(pageable);
    }

    public Teacher findById(int id) {
        logger.debug("Find teacher by id {}", id);
        return teacherDao.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any teacher with id: " + id));
    }

    public void save(Teacher teacher) {
        logger.debug("Save teacher");
        uniqueCheck(teacher);
        teacherDao.save(teacher);
    }

    public void deleteById(int id) {
        logger.debug("Delete teacher by id: {}", id);
        teacherDao.deleteById(id);
    }

    private void uniqueCheck(Teacher teacher) {
        logger.debug("Check teacher is unique");

        Optional<Teacher> existingTeacher = teacherDao.findByFullNameAndBirthDate(teacher.getFirstName(),
            teacher.getLastName(), teacher.getBirthDate());
        if (existingTeacher.isPresent() && (existingTeacher.get().getId() != teacher.getId())) {
            throw new EntityNotUniqueException("Teacher with full name " + teacher.getFirstName() + " "
                + teacher.getLastName() + " and birth date " + teacher.getBirthDate() + " is already exists!");
        }
    }

    public List<Teacher> findTeachersForChange(Lecture lecture) {
        logger.debug("Find teachers who havent lectures and vacation this periodand who can teach this subject");
        return teacherDao.findByFreeDateAndSubjectWithCurrentTeacher(lecture.getDate(), lecture.getTime(),
            lecture.getSubject());
    }
}
