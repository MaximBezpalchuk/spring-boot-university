package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.LectureTimeMapper;
import com.foxminded.university.dto.LectureTimeDto;
import com.foxminded.university.model.LectureTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void shouldMapLectureTimeDtoToLectureTime() {
        // given
        LectureTimeDto lectureTimeDto = new LectureTimeDto(1, LocalTime.of(8, 0), LocalTime.of(9, 45));
        // when
        LectureTime lectureTime = lectureTimeMapper.dtoToLectureTime(lectureTimeDto);
        // then
        assertNotNull(lectureTime);
        assertEquals(lectureTime.getId(), 1);
        assertEquals(lectureTime.getStart(), LocalTime.of(8, 0));
        assertEquals(lectureTime.getEnd(), LocalTime.of(9, 45));
    }
}
