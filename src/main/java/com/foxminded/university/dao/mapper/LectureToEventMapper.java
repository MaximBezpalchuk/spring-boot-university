package com.foxminded.university.dao.mapper;

import com.foxminded.university.model.Event;
import com.foxminded.university.model.Lecture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class LectureToEventMapper {

    @Mapping(target = "title", source = "lecture.subject.name")
    @Mapping(target = "start", expression = "java(lecture.getDate().atTime(lecture.getTime().getStart()))")
    @Mapping(target = "end", expression = "java(lecture.getDate().atTime(lecture.getTime().getEnd()))")
    @Mapping(target = "url", expression = "java(\"/lectures/\" + lecture.getId())")
    public abstract Event lectureToEvent(Lecture lecture);
}
