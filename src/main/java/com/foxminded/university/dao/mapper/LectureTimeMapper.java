package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.LectureTimeDto;
import com.foxminded.university.model.LectureTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class LectureTimeMapper {

    @Mapping(target = "id", source = "lectureTime.id")
    @Mapping(target = "start", source = "lectureTime.start")
    @Mapping(target = "end", source = "lectureTime.end")
    public abstract LectureTimeDto lectureTimeToDto(LectureTime lectureTime);

    @Mapping(target = "id", source = "lectureTimeDto.id")
    @Mapping(target = "start", source = "lectureTimeDto.start")
    @Mapping(target = "end", source = "lectureTimeDto.end")
    public abstract LectureTime dtoToLectureTime(LectureTimeDto lectureTimeDto);
}
