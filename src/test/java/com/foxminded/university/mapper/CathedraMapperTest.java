package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.model.Cathedra;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void shouldMapCathedraDtoToCathedra() {
        // given
        CathedraDto cathedraDto = new CathedraDto(1, "Fantastic Cathedra");
        // when
        Cathedra cathedra = cathedraMapper.dtoToCathedra(cathedraDto);
        // then
        assertNotNull(cathedra);
        assertEquals(cathedra.getId(), 1);
        assertEquals(cathedra.getName(), "Fantastic Cathedra");
    }
}
