package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.StudentDto;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
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
        assertEquals(studentDto.getGroupDto().getId(), 1);
        assertEquals(studentDto.getGroupDto().getCathedraDto().getId(), 1);
    }
}
