package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", imports = {Arrays.class, Collectors.class})
public abstract class LectureMapper {

    @Autowired
    protected TeacherService teacherService;
    @Autowired
    protected TeacherMapper teacherMapper;
    @Autowired
    protected CathedraService cathedraService;
    @Autowired
    protected GroupService groupService;
    @Autowired
    protected AudienceService audienceService;
    @Autowired
    protected SubjectService subjectService;
    @Autowired
    protected LectureTimeService lectureTimeService;

    @Mapping(target = "cathedraName", source = "lecture.cathedra.name")
    @Mapping(target = "groupNames", expression = "java(lecture.getGroups().stream().map(Group::getName).collect(Collectors.toList()))")
    @Mapping(target = "teacherDto", expression = "java(teacherMapper.teacherToDto(lecture.getTeacher()))")
    @Mapping(target = "audienceRoom", source = "lecture.audience.room")
    @Mapping(target = "subjectName", source = "lecture.subject.name")
    @Mapping(target = "start", source = "lecture.time.start")
    @Mapping(target = "end", source = "lecture.time.end")
    public abstract LectureDto lectureToDto(Lecture lecture);

    @Mapping(target = "cathedra", expression = "java(cathedraService.findByName(lectureDto.getCathedraName()))")
    @Mapping(target = "group", expression = "java(lectureDto.getGroupNames().stream().map(groupService::findByName).collect(Collectors.toList()))")
    @Mapping(target = "teacher", expression = "java(teacherService.findByFirstNameAndLastNameAndBirthDate(lectureDto.getTeacherDto().getFirstName(), lectureDto.getTeacherDto().getLastName(), lectureDto.getTeacherDto().getBirthDate()))")
    @Mapping(target = "audience", expression = "java(audienceService.findByRoom(lectureDto.getAudienceRoom()))")
    @Mapping(target = "subject", expression = "java(subjectService.findByName(lectureDto.getSubjectName()))")
    @Mapping(target = "time", expression = "java(lectureTimeService.findByStartAndEnd(lectureDto.getStart(), lectureDto.getEnd()))")
    public abstract Lecture dtoToLecture(LectureDto lectureDto);

    public abstract GroupDto map(Group group);

    public abstract Group map(GroupDto groupDto);
}
