package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.dto.StudentDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StudentMapperTest {

    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private GroupMapper groupMapper = new GroupMapperImpl(cathedraMapper);
    private StudentMapper studentMapper = new StudentMapperImpl(groupMapper);

    @Test
    void shouldMapStudentToStudentDto() {
        // given
        Cathedra cathedra = Cathedra.builder().id(1).build();
        Group group = Group.builder().id(1).cathedra(cathedra).build();
        Student student = Student.builder()
                .id(1)
                .firstName("TestName")
                .lastName("TestLastName")
                .phone("88005553535")
                .address("Address")
                .email("one@mail.com")
                .gender(Gender.MALE)
                .postalCode("123")
                .education("Edu")
                .birthDate(LocalDate.of(2020,1,1))
                .group(group)
                .build();
        // when
        StudentDto studentDto = studentMapper.studentToDto(student);
        // then
        assertNotNull(studentDto);
        assertEquals(studentDto.getId(), 1);
        assertEquals(studentDto.getFirstName(), "TestName");
        assertEquals(studentDto.getLastName(), "TestLastName");
        assertEquals(studentDto.getPhone(), "88005553535");
        assertEquals(studentDto.getAddress(), "Address");
        assertEquals(studentDto.getEmail(), "one@mail.com");
        assertEquals(studentDto.getGender(), Gender.MALE);
        assertEquals(studentDto.getPostalCode(), "123");
        assertEquals(studentDto.getEducation(), "Edu");
        assertEquals(studentDto.getBirthDate(), LocalDate.of(2020,1,1));
        assertEquals(studentDto.getGroup().getId(), 1);
        assertEquals(studentDto.getGroup().getCathedra().getId(), 1);
    }

    @Test
    void shouldMapStudentDtoToStudent() {
        // given
        CathedraDto cathedraDto = new CathedraDto(1, "Fantastic Cathedra");
        GroupDto groupDto = new GroupDto(1, "Killers", cathedraDto);
        StudentDto studentDto = new StudentDto(1, "TestName", "TestLastName", "88005553535", "Address", "one@mail.com", Gender.MALE, "123", "Edu", LocalDate.of(2020,1,1), groupDto);
        // when
        Student student = studentMapper.dtoToStudent(studentDto);
        // then
        assertNotNull(student);
        assertEquals(student.getId(), 1);
        assertEquals(student.getFirstName(), "TestName");
        assertEquals(student.getLastName(), "TestLastName");
        assertEquals(student.getPhone(), "88005553535");
        assertEquals(student.getAddress(), "Address");
        assertEquals(student.getEmail(), "one@mail.com");
        assertEquals(student.getGender(), Gender.MALE.toString());
        assertEquals(student.getPostalCode(), "123");
        assertEquals(student.getEducation(), "Edu");
        assertEquals(student.getBirthDate(), LocalDate.of(2020,1,1));
        assertEquals(student.getGroup().getId(), 1);
        assertEquals(student.getGroup().getCathedra().getId(), 1);
    }
}
