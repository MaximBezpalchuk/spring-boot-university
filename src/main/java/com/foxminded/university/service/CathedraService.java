package com.foxminded.university.service;

import com.foxminded.university.dao.CathedraDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Cathedra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CathedraService {

    private static final Logger logger = LoggerFactory.getLogger(CathedraService.class);

    private final CathedraDao cathedraDao;

    public CathedraService(CathedraDao cathedraDao) {
        this.cathedraDao = cathedraDao;
    }

    public List<Cathedra> findAll() {
        logger.debug("Find all cathedras");
        return cathedraDao.findAll();
    }

    public Cathedra findById(int id) {
        logger.debug("Find cathedra by id {}", id);
        return cathedraDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find any cathedra with id: " + id));
    }

    public void save(Cathedra cathedra) {
        logger.debug("Save cathedra");
        uniqueCheck(cathedra);
        cathedraDao.save(cathedra);
    }

    public void delete(Cathedra cathedra) {
        logger.debug("Delete cathedra with id: {}", cathedra.getId());
        cathedraDao.delete(cathedra);
    }

    private void uniqueCheck(Cathedra cathedra) {
        logger.debug("Check catheda is unique");
        Optional<Cathedra> existingCathedra = cathedraDao.findByName(cathedra.getName());
        if (existingCathedra.isPresent() && existingCathedra.get().getId() != cathedra.getId()) {
            throw new EntityNotUniqueException("Cathedra with name " + cathedra.getName() + " is already exists!");
        }
    }
}
