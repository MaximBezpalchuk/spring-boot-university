package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dao.mapper.AudienceMapperImpl;
import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AudienceMapperTest {

    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private AudienceMapper audienceMapper = new AudienceMapperImpl(cathedraMapper);

    @Test
    void shouldMapAudienceToAudienceDto() {
        // given
        Cathedra cathedra = Cathedra.builder().id(1).build();
        Audience audience = Audience.builder().id(1).room(1).capacity(10).cathedra(cathedra).build();
        // when
        AudienceDto audienceDto = audienceMapper.audienceToDto(audience);
        // then
        assertNotNull(audienceDto);
        assertEquals(audienceDto.getId(), 1);
        assertEquals(audienceDto.getRoom(), 1);
        assertEquals(audienceDto.getCapacity(), 10);
        assertEquals(audienceDto.getCathedra().getId(), 1);
    }

    @Test
    void shouldMapAudienceDtoToAudience() {
        // given
        CathedraDto cathedraDto = new CathedraDto(1, "Fantastic Cathedra");
        AudienceDto audienceDto = new AudienceDto(1, 1, 10, cathedraDto);
        // when
        Audience audience = audienceMapper.dtoToAudience(audienceDto);
        // then
        assertNotNull(audience);
        assertEquals(audience.getId(), 1);
        assertEquals(audience.getRoom(), 1);
        assertEquals(audience.getCapacity(), 10);
        assertEquals(audience.getCathedra().getId(), 1);
    }
}
