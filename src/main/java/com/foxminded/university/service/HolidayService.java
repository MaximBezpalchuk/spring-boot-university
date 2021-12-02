package com.foxminded.university.service;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HolidayService {

    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

    private HolidayDao holidayDao;

    public HolidayService(HolidayDao holidayDao) {
        this.holidayDao = holidayDao;
    }

    public List<Holiday> findAll() {
        logger.debug("Find all holidays");
        return holidayDao.findAll();
    }

    public Page<Holiday> findAll(final Pageable pageable) {
        logger.debug("Find all holidays paginated");
        return holidayDao.findPaginatedHolidays(pageable);
    }

    public Holiday findById(int id) {
        logger.debug("Find holiday by id {}", id);
        return holidayDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find any holiday with id: " + id));
    }

    public void save(Holiday holiday) {
        logger.debug("Save holiday");
        uniqueCheck(holiday);
        holidayDao.save(holiday);
    }

    public void deleteById(int id) {
        logger.debug("Delete holiday by id: {}", id);
        holidayDao.deleteById(id);
    }

    private void uniqueCheck(Holiday holiday) {
        logger.debug("Check holiday is unique");
        Optional<Holiday> existingHoliday = holidayDao.findByNameAndDate(holiday.getName(), holiday.getDate());

        if (existingHoliday.isPresent() && (existingHoliday.get().getId() != holiday.getId())) {
            throw new EntityNotUniqueException("Holiday with name " + holiday.getName() + " and date "
                    + holiday.getDate() + " is already exists!");
        }
    }
}
