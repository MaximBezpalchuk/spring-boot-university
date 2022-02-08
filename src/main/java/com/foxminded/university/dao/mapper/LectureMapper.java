package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.LectureTimeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", imports = {Arrays.class, Collectors.class})
public abstract class LectureMapper {

    @Autowired
    protected TeacherMapper teacherMapper;
    @Autowired
    protected AudienceMapper audienceMapper;
    @Autowired
    protected SubjectMapper subjectMapper;
    @Autowired
    protected LectureTimeMapper lectureTimeMapper;
    @Autowired
    protected CathedraMapper cathedraMapper;
    @Autowired
    protected GroupMapper groupMapper;

    @Mapping(target = "cathedraDto", expression = "java(cathedraMapper.cathedraToDto(lecture.getCathedra()))")
    @Mapping(target = "groupDtos", expression = "java(lecture.getGroups().stream().map(groupMapper::groupToDto).collect(Collectors.toList()))")
    @Mapping(target = "teacherDto", expression = "java(teacherMapper.teacherToDto(lecture.getTeacher()))")
    @Mapping(target = "audienceDto", expression = "java(audienceMapper.audienceToDto(lecture.getAudience()))")
    @Mapping(target = "subjectDto", expression = "java(subjectMapper.subjectToDto(lecture.getSubject()))")
    @Mapping(target = "lectureTimeDto", expression = "java(lectureTimeMapper.lectureTimeToDto(lecture.getTime()))")
    public abstract LectureDto lectureToDto(Lecture lecture);

    @Mapping(target = "cathedra", expression = "java(cathedraMapper.dtoToCathedra(lectureDto.getCathedraDto()))")
    @Mapping(target = "group", expression = "java(lectureDto.getGroupDtos().stream().map(groupMapper::dtoToGroup).collect(Collectors.toList()))")
    @Mapping(target = "teacher", expression = "java(teacherMapper.dtoToTeacher(lectureDto.getTeacherDto()))")
    @Mapping(target = "audience", expression = "java(audienceMapper.dtoToAudience(lectureDto.getAudienceDto()))")
    @Mapping(target = "subject", expression = "java(subjectMapper.dtoToSubject(lectureDto.getSubjectDto()))")
    @Mapping(target = "time", expression = "java(lectureTimeMapper.dtoToLectureTime(lectureDto.getLectureTimeDto()))")
    public abstract Lecture dtoToLecture(LectureDto lectureDto);
}
