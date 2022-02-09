package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.model.Cathedra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CathedraMapperTest {

    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);

    @Test
    void shouldMapCathedraToCathedraDto() {
        // given
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        // when
        CathedraDto cathedraDto = cathedraMapper.cathedraToDto(cathedra);
        // then
        assertNotNull(cathedraDto);
        assertEquals(cathedraDto.getId(), 1);
        assertEquals(cathedraDto.getName(), "Fantastic Cathedra");
    }
}
