package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.TeacherDto;
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
        assertEquals(teacherDto.getCathedraDto().getId(), 1);
        assertEquals(teacherDto.getSubjectDtos().get(0).getId(), 1);
        assertEquals(teacherDto.getSubjectDtos().get(0).getCathedraDto().getId(), 1);
        assertEquals(teacherDto.getDegree(), Degree.PROFESSOR);
    }
}
