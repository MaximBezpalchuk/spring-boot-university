package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TeacherMapperTest {

    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private SubjectMapper subjectMapper = new SubjectMapperImpl(cathedraMapper);
    private TeacherMapper teacherMapper = new TeacherMapperImpl(cathedraMapper, subjectMapper);

    @Test
    void shouldMapAudienceToAudienceDto() {
        // given
        Cathedra cathedra = Cathedra.builder().id(1).build();
        Subject subject = Subject.builder().id(1).cathedra(cathedra).build();
        Teacher teacher = Teacher.builder()
                .id(1)
                .firstName("TestName")
                .lastName("TestLastName")
                .phone("88005553535")
                .address("Address")
                .email("one@mail.com")
                .gender(Gender.MALE)
                .postalCode("123")
                .education("Edu")
                .birthDate(LocalDate.of(2020, 1, 1))
                .cathedra(cathedra)
                .subjects(Arrays.asList(subject))
                .degree(Degree.PROFESSOR)
                .build();
        // when
        TeacherDto teacherDto = teacherMapper.teacherToDto(teacher);
        // then
        assertNotNull(teacherDto);
        assertEquals(teacherDto.getId(), 1);
        assertEquals(teacherDto.getFirstName(), "TestName");
        assertEquals(teacherDto.getLastName(), "TestLastName");
        assertEquals(teacherDto.getPhone(), "88005553535");
        assertEquals(teacherDto.getAddress(), "Address");
        assertEquals(teacherDto.getEmail(), "one@mail.com");
        assertEquals(teacherDto.getGender(), Gender.MALE);
        assertEquals(teacherDto.getPostalCode(), "123");
        assertEquals(teacherDto.getEducation(), "Edu");
        assertEquals(teacherDto.getBirthDate(), LocalDate.of(2020, 1, 1));
        assertEquals(teacherDto.getCathedra().getId(), 1);
        assertEquals(teacherDto.getSubjects().get(0).getId(), 1);
        assertEquals(teacherDto.getSubjects().get(0).getCathedra().getId(), 1);
        assertEquals(teacherDto.getDegree(), Degree.PROFESSOR);
    }

    @Test
    void shouldMapAudienceDtoToAudience() {
        // given
        CathedraDto cathedraDto = new CathedraDto(1, "Fantastic Cathedra");
        SubjectDto subjectDto = new SubjectDto(1, cathedraDto, "Subject Name", "Subject desc" );
        TeacherDto teacherDto = new TeacherDto(1, "TestName", "TestLastName", "88005553535", "Address", "one@mail.com", Gender.MALE, "123", "Edu", LocalDate.of(2020,1,1), cathedraDto, Arrays.asList(subjectDto), Degree.PROFESSOR);
        // when
        Teacher teacher = teacherMapper.dtoToTeacher(teacherDto);
        // then
        assertNotNull(teacher);
        assertEquals(teacher.getId(), 1);
        assertEquals(teacher.getFirstName(), "TestName");
        assertEquals(teacher.getLastName(), "TestLastName");
        assertEquals(teacher.getPhone(), "88005553535");
        assertEquals(teacher.getAddress(), "Address");
        assertEquals(teacher.getEmail(), "one@mail.com");
        assertEquals(teacher.getGender(), Gender.MALE.toString());
        assertEquals(teacher.getPostalCode(), "123");
        assertEquals(teacher.getEducation(), "Edu");
        assertEquals(teacher.getBirthDate(), LocalDate.of(2020, 1, 1));
        assertEquals(teacher.getCathedra().getId(), 1);
        assertEquals(teacher.getSubjects().get(0).getId(), 1);
        assertEquals(teacher.getSubjects().get(0).getCathedra().getId(), 1);
        assertEquals(teacher.getDegree(), Degree.PROFESSOR);
    }
}
