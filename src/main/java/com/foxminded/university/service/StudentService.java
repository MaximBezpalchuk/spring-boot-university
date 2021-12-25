package com.foxminded.university.service;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.StudentRepository;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.exception.GroupOverflowException;
import com.foxminded.university.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private UniversityConfigProperties universityConfig;

    public StudentService(StudentRepository studentRepository, UniversityConfigProperties universityConfig) {
        this.studentRepository = studentRepository;
        this.universityConfig = universityConfig;
    }

    public List<Student> findAll() {
        logger.debug("Find all students");
        return studentRepository.findAll();
    }

    public Page<Student> findAll(final Pageable pageable) {
        logger.debug("Find all holidays paginated");
        return studentRepository.findAll(pageable);
    }

    public Student findById(int id) {
        logger.debug("Find student by id {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find any student with id: " + id));
    }

    public void save(Student student) {
        logger.debug("Save student");
        uniqueCheck(student);
        groupOverflowCheck(student);
        studentRepository.save(student);
    }

    public void delete(Student student) {
        logger.debug("Delete student with id: {}", student.getId());
        studentRepository.delete(student);
    }

    private void uniqueCheck(Student student) {
        logger.debug("Check student is unique");
        Optional<Student> existingStudent = studentRepository.findByFullNameAndBirthDate(student.getFirstName(),
                student.getLastName(), student.getBirthDate());

        if (existingStudent.isPresent() && (existingStudent.get().getId() != student.getId())) {
            throw new EntityNotUniqueException("Student with full name " + student.getFirstName() + " "
                    + student.getLastName() + " and  birth date " + student.getBirthDate() + " is already exists!");
        }
    }

    private void groupOverflowCheck(Student student) {
        logger.debug("Check that group is filled");
        if (student.getGroup() != null) {
            int groupSize = studentRepository.findByGroupId(student.getGroup().getId()).size();
            if (groupSize >= universityConfig.getMaxGroupSize()) {
                throw new GroupOverflowException("This group is already full! Group size is: " + groupSize
                        + ". Max group size is: " + universityConfig.getMaxGroupSize());
            }
        }
    }
}
