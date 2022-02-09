package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.model.Lecture;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class, GroupMapper.class, TeacherMapper.class, AudienceMapper.class, SubjectMapper.class, LectureTimeMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LectureMapper {

    @Mapping(target = "cathedraDto", source = "lecture.cathedra")
    @Mapping(target = "groupDtos", source = "lecture.groups")
    @Mapping(target = "teacherDto", source = "lecture.teacher")
    @Mapping(target = "audienceDto", source = "lecture.audience")
    @Mapping(target = "subjectDto", source = "lecture.subject")
    @Mapping(target = "lectureTimeDto", source = "lecture.time")
    LectureDto lectureToDto(Lecture lecture);

    @Mapping(target = "cathedra", source = "lectureDto.cathedraDto")
    @Mapping(target = "group", source = "lectureDto.groupDtos")
    @Mapping(target = "teacher", source = "lectureDto.teacherDto")
    @Mapping(target = "audience", source = "lectureDto.audienceDto")
    @Mapping(target = "subject", source = "lectureDto.subjectDto")
    @Mapping(target = "time", source = "lectureDto.lectureTimeDto")
    Lecture dtoToLecture(LectureDto lectureDto);
}
