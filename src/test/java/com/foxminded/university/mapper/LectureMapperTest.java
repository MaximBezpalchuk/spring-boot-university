package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class LectureMapperTest {

    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private GroupMapper groupMapper = new GroupMapperImpl(cathedraMapper);
    private SubjectMapper subjectMapper = new SubjectMapperImpl(cathedraMapper);
    private TeacherMapper teacherMapper = new TeacherMapperImpl(cathedraMapper, subjectMapper);
    private AudienceMapper audienceMapper = new AudienceMapperImpl(cathedraMapper);
    private LectureTimeMapper lectureTimeMapper = Mappers.getMapper(LectureTimeMapper.class);
    private LectureMapper lectureMapper = new LectureMapperImpl(cathedraMapper, groupMapper, teacherMapper, audienceMapper, subjectMapper, lectureTimeMapper);

    @Test
    void shouldMapVacationToVacationDto() {
        // given
        Cathedra cathedra = Cathedra.builder().id(1).build();
        Group group = Group.builder().id(1).cathedra(cathedra).build();
        Subject subject = Subject.builder().id(1).cathedra(cathedra).build();
        Teacher teacher = Teacher.builder().id(1).gender(Gender.MALE).cathedra(cathedra).subjects(Arrays.asList(subject)).degree(Degree.PROFESSOR).build();
        Audience audience = Audience.builder().id(1).cathedra(cathedra).build();
        LectureTime lectureTime = LectureTime.builder().id(1).build();
        Lecture lecture = Lecture.builder()
                .id(1)
                .audience(audience)
                .cathedra(cathedra)
                .date(LocalDate.of(2021, 1, 1))
                .group(Arrays.asList(group))
                .subject(subject)
                .teacher(teacher)
                .time(lectureTime)
                .build();
        // when
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture);
        // then
        assertNotNull(lectureDto);
        assertEquals(lectureDto.getId(), 1);
        assertEquals(lectureDto.getAudience().getId(), 1);
        assertEquals(lectureDto.getAudience().getCathedra().getId(), 1);
        assertEquals(lectureDto.getCathedra().getId(), 1);
        assertEquals(lectureDto.getDate(), LocalDate.of(2021, 1, 1));
        assertEquals(lectureDto.getGroups().get(0).getId(), 1);
        assertEquals(lectureDto.getGroups().get(0).getCathedra().getId(), 1);
        assertEquals(lectureDto.getSubject().getId(), 1);
        assertEquals(lectureDto.getSubject().getCathedra().getId(), 1);
        assertEquals(lectureDto.getTeacher().getId(), 1);
        assertEquals(lectureDto.getTeacher().getCathedra().getId(), 1);
        assertEquals(lectureDto.getTeacher().getSubjects().get(0).getId(), 1);
        assertEquals(lectureDto.getTeacher().getSubjects().get(0).getCathedra().getId(), 1);
        assertEquals(lectureDto.getTime().getId(), 1);
    }
}
