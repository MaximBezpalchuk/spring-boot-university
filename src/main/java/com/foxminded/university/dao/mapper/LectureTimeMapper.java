package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.LectureTimeDto;
import com.foxminded.university.model.LectureTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class LectureTimeMapper {

    public abstract LectureTimeDto lectureTimeToDto(LectureTime lectureTime);

    public abstract LectureTime dtoToLectureTime(LectureTimeDto lectureTimeDto);
}
