package com.foxminded.university.dao.mapper;

import com.foxminded.university.model.Event;
import com.foxminded.university.model.Lecture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class LectureToEventMapper {

    public static LectureToEventMapper INSTANCE = Mappers.getMapper(LectureToEventMapper.class);

    @Mapping(target = "title", source = "lecture.subject.name")
    @Mapping(target = "start", expression = "java(lecture.getDate().atTime(lecture.getTime().getStart()))")
    @Mapping(target = "end", expression = "java(lecture.getDate().atTime(lecture.getTime().getEnd()))")
    @Mapping(target = "url", expression = "java(\"/university/lectures/\" + lecture.getId())")
    public abstract Event lectureToEvent(Lecture lecture);
}
