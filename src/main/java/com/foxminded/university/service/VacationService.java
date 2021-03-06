package com.foxminded.university.service;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.VacationRepository;
import com.foxminded.university.exception.ChosenDurationException;
import com.foxminded.university.exception.DurationException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Vacation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class VacationService {

    private static final Logger logger = LoggerFactory.getLogger(VacationService.class);

    private final VacationRepository vacationRepository;
    private UniversityConfigProperties universityConfig;

    public VacationService(VacationRepository vacationRepository, UniversityConfigProperties universityConfig) {
        this.vacationRepository = vacationRepository;
        this.universityConfig = universityConfig;
    }

    public List<Vacation> findAll() {
        logger.debug("Find all vacations");
        return vacationRepository.findAll();
    }

    public Vacation findByTeacherIdAndId(int teacherId, int id) {
        logger.debug("Find vacation by teacher id {} and id {}", teacherId, id);
        return vacationRepository.findByTeacherIdAndId(teacherId, id)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any vacation with id: " + id));
    }

    public Vacation save(Vacation vacation) {
        logger.debug("Save vacation");
        uniqueCheck(vacation);
        dateCorrectCheck(vacation);
        dateMoreThenOneDayCheck(vacation);
        vacationDurationLessOrEqualsThanMaxCheck(vacation);

        return vacationRepository.save(vacation);
    }

    public void delete(int id) {
        logger.debug("Delete vacation with id: {}", id);
        vacationRepository.deleteById(id);
    }

    public List<Vacation> findByTeacherId(int id) {
        logger.debug("Find vacation by teacher id: {}", id);
        return vacationRepository.findByTeacherId(id);
    }

    public Page<Vacation> findByTeacherId(final Pageable pageable, int id) {
        logger.debug("Find all vacations paginated by teacher id");
        return vacationRepository.findAllByTeacherId(pageable, id);
    }

    private void uniqueCheck(Vacation vacation) {
        logger.debug("Check vacation is unique");
        Optional<Vacation> existingVacation = vacationRepository.findByStartAndEndAndTeacher(vacation.getStart(), vacation.getEnd(),
            vacation.getTeacher());

        if (existingVacation.isPresent() && (existingVacation.get().getId() != vacation.getId())) {
            throw new EntityNotUniqueException("Vacation with start(" + vacation.getStart() + "), end("
                + vacation.getEnd() + ") and teacher(" + vacation.getTeacher().getFirstName() + " "
                + vacation.getTeacher().getLastName() + ") id is already exists!");
        }
    }

    private void dateCorrectCheck(Vacation vacation) {
        logger.debug("Check vacation start is after end");
        if (!vacation.getStart().isBefore(vacation.getEnd()) && !vacation.getStart().equals(vacation.getEnd())) {
            throw new DurationException("Vacation start date can`t be after vacation end date! Vacation start is: "
                + vacation.getStart() + ". Vacation end is: " + vacation.getEnd());
        }
    }

    private void dateMoreThenOneDayCheck(Vacation vacation) {
        logger.debug("Check vacation duration more or equals 1 day");
        if (getVacationDaysCount(vacation) < 1) {
            throw new DurationException("Vacation can`t be less than 1 day! Vacation start is: " + vacation.getStart()
                + ". Vacation end is: " + vacation.getEnd());
        }
    }

    private long getVacationDaysCount(Vacation vacation) {
        return Math.abs(ChronoUnit.DAYS.between(vacation.getStart(), vacation.getEnd()));
    }

    private void vacationDurationLessOrEqualsThanMaxCheck(Vacation vacation) {
        logger.debug("Check vacation duration less or equals than max");
        long teacherVacationDays = vacationRepository
            .findByTeacherAndYear(vacation.getTeacher(), vacation.getStart().getYear()).stream()
            .map(vac -> getVacationDaysCount(vac)).mapToLong(Long::longValue).sum();

        if ((teacherVacationDays + getVacationDaysCount(vacation)) >= universityConfig.getMaxVacation()
            .get(vacation.getTeacher().getDegree())) {
            throw new ChosenDurationException("Vacations duration(existing " + teacherVacationDays + " plus appointed "
                + getVacationDaysCount(vacation) + ") can`t be more than max("
                + universityConfig.getMaxVacation().get(vacation.getTeacher().getDegree()) + ") for degree "
                + vacation.getTeacher().getDegree() + "!");
        }
    }
}
