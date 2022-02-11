package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.LectureTimeDto;
import com.foxminded.university.model.LectureTime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LectureTimeMapper {

    LectureTimeDto lectureTimeToDto(LectureTime lectureTime);

    LectureTime dtoToLectureTime(LectureTimeDto lectureTimeDto);
}
