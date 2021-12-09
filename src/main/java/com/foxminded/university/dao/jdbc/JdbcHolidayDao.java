package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.jdbc.mapper.HolidayRowMapper;
import com.foxminded.university.model.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcHolidayDao implements HolidayDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcHolidayDao.class);

    private static final String SELECT_ALL = "SELECT * FROM holidays";
    private static final String COUNT_ALL = "SELECT COUNT(*) FROM holidays";
    private static final String SELECT_BY_PAGE = "SELECT * FROM holidays LIMIT ? OFFSET ?";
    private static final String SELECT_BY_ID = "SELECT * FROM holidays WHERE id = ?";
    private static final String INSERT_HOLIDAY = "INSERT INTO holidays(name, date, cathedra_id) VALUES(?, ?, ?)";
    private static final String UPDATE_HOLIDAY = "UPDATE holidays SET name=?, date=?, cathedra_id=? WHERE id=?";
    private static final String DELETE_HOLIDAY = "DELETE FROM holidays WHERE id = ?";
    private static final String SELECT_BY_NAME_AND_DATE = "SELECT * FROM holidays WHERE name = ? AND date = ?";
    private static final String SELECT_BY_DATE = "SELECT * FROM holidays WHERE date = ?";

    private final JdbcTemplate jdbcTemplate;
    private final HolidayRowMapper rowMapper;

    public JdbcHolidayDao(JdbcTemplate jdbcTemplate, HolidayRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Holiday> findAll() {
        logger.debug("Find all holidays");
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }

    @Override
    public Page<Holiday> findPaginatedHolidays(Pageable pageable) {
        logger.debug("Find all holidays with pageSize:{} and offset:{}", pageable.getPageSize(), pageable.getOffset());
        int total = jdbcTemplate.queryForObject(COUNT_ALL, Integer.class);
        List<Holiday> holidays = jdbcTemplate.query(SELECT_BY_PAGE, rowMapper, pageable.getPageSize(),
            pageable.getOffset());

        return new PageImpl<>(holidays, pageable, total);
    }

    @Override
    public Optional<Holiday> findById(int id) {
        logger.debug("Find holiday by id: {}", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Holiday holiday) {
        logger.debug("Save holiday {}", holiday);
        if (holiday.getId() == 0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(INSERT_HOLIDAY,
                    Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, holiday.getName());
                statement.setObject(2, holiday.getDate());
                statement.setInt(3, holiday.getCathedra().getId());
                return statement;
            }, keyHolder);
            holiday.setId((int) keyHolder.getKeyList().get(0).get("id"));
            logger.debug("New holiday created with id: {}", holiday.getId());
        } else {
            jdbcTemplate.update(UPDATE_HOLIDAY, holiday.getName(), holiday.getDate(), holiday.getCathedra().getId(),
                holiday.getId());
            logger.debug("Holiday with id {} was updated", holiday.getId());
        }
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update(DELETE_HOLIDAY, id);
        logger.debug("Holiday with id {} was deleted", id);
    }

    @Override
    public Optional<Holiday> findByNameAndDate(String name, LocalDate date) {
        logger.debug("Find holiday with name: {} and date: {}", name, date);
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_NAME_AND_DATE, rowMapper, name, date));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Holiday> findByDate(LocalDate date) {
        logger.debug("Find holiday by date: {}", date);
        return jdbcTemplate.query(SELECT_BY_DATE, rowMapper, date);
    }
}
