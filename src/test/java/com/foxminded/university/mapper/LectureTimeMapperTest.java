package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.LectureTimeMapper;
import com.foxminded.university.dto.LectureTimeDto;
import com.foxminded.university.model.LectureTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class LectureTimeMapperTest {

    private LectureTimeMapper lectureTimeMapper = Mappers.getMapper(LectureTimeMapper.class);

    @Test
    void shouldMapLectureTimeToLectureTimeDto() {
        // given
        LectureTime lectureTime = LectureTime.builder()
                .id(1)
                .start(LocalTime.of(8, 0))
                .end(LocalTime.of(9, 45))
                .build();
        // when
        LectureTimeDto lectureTimeDto = lectureTimeMapper.lectureTimeToDto(lectureTime);
        // then
        assertNotNull(lectureTimeDto);
        assertEquals(lectureTimeDto.getId(), 1);
        assertEquals(lectureTimeDto.getStart(), LocalTime.of(8, 0));
        assertEquals(lectureTimeDto.getEnd(), LocalTime.of(9, 45));
    }
}
