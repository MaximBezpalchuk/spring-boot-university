package com.foxminded.university.service;

import com.foxminded.university.dao.CathedraRepository;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Cathedra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CathedraServiceTest {

    @Mock
    private CathedraRepository cathedraRepository;
    @InjectMocks
    private CathedraService cathedraService;

    @Test
    void givenListOfCathedras_whenFindAll_thenAllExistingCathedrasFound() {
        List<Cathedra> expected = Arrays.asList(Cathedra.builder().id(1).build());
        when(cathedraRepository.findAll()).thenReturn(expected);
        List<Cathedra> actual = cathedraService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingCathedra_whenFindById_thenCathedraFound() throws EntityNotFoundException {
        Optional<Cathedra> expected = Optional.of(Cathedra.builder().id(1).build());
        when(cathedraRepository.findById(1)).thenReturn(expected);
        Cathedra actual = cathedraService.findById(1);

        assertEquals(expected.get(), actual);
    }

    @Test
    void givenExistingCathedra_whenFindById_thenEntityNotFoundException() {
        when(cathedraRepository.findById(10)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            cathedraService.findById(10);
        });

        assertEquals("Can`t find any cathedra with id: 10", exception.getMessage());
    }

    @Test
    void givenNewCathedra_whenSave_thenSaved() {
        Cathedra cathedra = Cathedra.builder().build();
        cathedraService.save(cathedra);

        verify(cathedraRepository).save(cathedra);
    }

    @Test
    void givenExistingCathedra_whenSave_thenSaved() {
        Cathedra cathedra = Cathedra.builder().name("TestName").build();
        when(cathedraRepository.findByName(cathedra.getName())).thenReturn(Optional.of(cathedra));
        cathedraService.save(cathedra);

        verify(cathedraRepository).save(cathedra);
    }

    @Test
    void givenExistingAudienceId_whenDelete_thenDeleted() {
        Cathedra cathedra = Cathedra.builder().id(1).build();
        cathedraService.delete(cathedra);

        verify(cathedraRepository).delete(cathedra);
    }

    @Test
    void givenNotUniqueCathedra_whenSave_thenEntityNotUniqueException() {
        Cathedra cathedra1 = Cathedra.builder().id(1).name("Test1").build();
        Cathedra cathedra2 = Cathedra.builder().id(2).name("Test2").build();
        when(cathedraRepository.findByName(cathedra1.getName())).thenReturn(Optional.of(cathedra2));
        Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
            cathedraService.save(cathedra1);
        });

        assertEquals("Cathedra with name Test1 is already exists!", exception.getMessage());
    }
}
