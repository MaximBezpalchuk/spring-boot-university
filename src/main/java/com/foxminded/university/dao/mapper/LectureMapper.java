package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.model.Lecture;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class, GroupMapper.class, TeacherMapper.class, AudienceMapper.class, SubjectMapper.class, LectureTimeMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LectureMapper {

    LectureDto lectureToDto(Lecture lecture);

    Lecture dtoToLecture(LectureDto lectureDto);
}
