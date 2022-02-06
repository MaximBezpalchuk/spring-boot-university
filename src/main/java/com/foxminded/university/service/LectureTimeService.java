package com.foxminded.university.service;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.LectureTimeRepository;
import com.foxminded.university.exception.ChosenDurationException;
import com.foxminded.university.exception.DurationException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.LectureTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class LectureTimeService {

    private static final Logger logger = LoggerFactory.getLogger(LectureTimeService.class);

    private final LectureTimeRepository lectureTimeRepository;
    private UniversityConfigProperties universityConfig;

    public LectureTimeService(LectureTimeRepository lectureTimeRepository, UniversityConfigProperties universityConfig) {
        this.lectureTimeRepository = lectureTimeRepository;
        this.universityConfig = universityConfig;
    }

    public List<LectureTime> findAll() {
        logger.debug("Find all lecture times");
        return lectureTimeRepository.findAll();
    }

    public LectureTime findById(int id) {
        logger.debug("Find lecture time by id {}", id);
        return lectureTimeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any lecture time with id: " + id));
    }

    public LectureTime findByStartAndEnd(LocalTime start, LocalTime end) {
        logger.debug("Find lecture time by start {} and end {}", start, end);
        return lectureTimeRepository.findByStartAndEnd(start, end)
            .orElseThrow(() -> new EntityNotFoundException("Can`t find any lecture time with start: " + start + " and end: " + end));
    }

    public LectureTime save(LectureTime lectureTime) {
        logger.debug("Save lecture time");
        uniqueCheck(lectureTime);
        timeCorrectCheck(lectureTime);
        durationMoreThanChosenTimeCheck(lectureTime);

        return lectureTimeRepository.save(lectureTime);

    }

    public void delete(LectureTime lectureTime) {
        logger.debug("Delete lecture time with id: {}", lectureTime.getId());
        lectureTimeRepository.delete(lectureTime);
    }

    private void uniqueCheck(LectureTime lectureTime) {
        logger.debug("Check lecture time is unique");
        Optional<LectureTime> existingLectureTime = lectureTimeRepository.findByStartAndEnd(lectureTime.getStart(),
            lectureTime.getEnd());

        if (existingLectureTime.isPresent() && (existingLectureTime.get().getId() != lectureTime.getId())) {
            throw new EntityNotUniqueException("Lecture time with start time " + lectureTime.getStart()
                + " and end time " + lectureTime.getEnd() + " is already exists!");
        }
    }

    private void timeCorrectCheck(LectureTime lectureTime) {
        logger.debug("Check that start time is after end time");
        if (!lectureTime.getStart().isBefore(lectureTime.getEnd())) {
            throw new DurationException("Lecture time`s start (" + lectureTime.getStart()
                + ") can`t be after lecture time`s end (" + lectureTime.getEnd() + ")!");
        }
    }

    private void durationMoreThanChosenTimeCheck(LectureTime lectureTime) {
        logger.debug("Check that duration is more than min lecture duration");
        long durationInMinutes = Duration.between(lectureTime.getStart(), lectureTime.getEnd()).toMinutes();
        if (durationInMinutes <= universityConfig.getMinLectureDurationInMinutes()) {
            throw new ChosenDurationException("Duration " + durationInMinutes
                + " minutes is less than min lecture duration (" + universityConfig.getMinLectureDurationInMinutes() + " minutes)!");
        }
    }
}
