package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.AudienceMapper;
import com.foxminded.university.dao.mapper.AudienceMapperImpl;
import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
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
        assertEquals(audienceDto.getCathedraDto().getId(), 1);
    }
}
