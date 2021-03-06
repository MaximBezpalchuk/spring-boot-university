package com.foxminded.university.service;

import com.foxminded.university.dao.AudienceRepository;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Audience;
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
public class AudienceServiceTest {

    @Mock
    private AudienceRepository audienceRepository;
    @InjectMocks
    private AudienceService audienceService;

    @Test
    void givenListOfAudiences_whenFindAll_thenAllExistingAudiencesFound() {
        Audience audience1 = Audience.builder().id(1).build();
        Audience audience2 = Audience.builder().id(2).build();
        List<Audience> expected = Arrays.asList(audience1, audience2);
        when(audienceRepository.findAll()).thenReturn(expected);
        List<Audience> actual = audienceService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingAudience_whenFindById_thenAudienceFound() {
        Optional<Audience> expected = Optional.of(Audience.builder().id(1).build());
        when(audienceRepository.findById(1)).thenReturn(expected);
        Audience actual = audienceService.findById(1);

        assertEquals(expected.get(), actual);
    }

    @Test
    void givenExistingAudience_whenFindById_thenEntityNotFoundException() {
        when(audienceRepository.findById(10)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            audienceService.findById(10);
        });

        assertEquals("Can`t find any audience with id: 10", exception.getMessage());
    }

    @Test
    void givenNewAudience_whenSave_thenSaved() {
        Audience audience = Audience.builder().build();
        audienceService.save(audience);

        verify(audienceRepository).save(audience);
    }

    @Test
    void givenExistingAudience_whenSave_thenSaved() {
        Audience audience = Audience.builder().id(1).room(123).build();
        when(audienceRepository.findByRoom(audience.getRoom())).thenReturn(Optional.of(audience));
        audienceService.save(audience);

        verify(audienceRepository).save(audience);
    }

    @Test
    void givenExistingAudienceId_whenDelete_thenDeleted() {
        Audience audience = Audience.builder().id(3).build();
        audienceService.delete(audience.getId());

        verify(audienceRepository).deleteById(audience.getId());
    }

    @Test
    void givenNotUniqueAudience_whenSave_thenEntityNotUniqueException() {
        Audience audience1 = Audience.builder()
            .id(1)
            .room(10)
            .build();
        Audience audience2 = Audience.builder()
            .id(5)
            .room(10)
            .build();
        when(audienceRepository.findByRoom(audience1.getRoom())).thenReturn(Optional.of(audience2));
        Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
            audienceService.save(audience1);
        });

        assertEquals("Audience with room number 10 is already exists!", exception.getMessage());
    }
}
