package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.dto.TeacherDto;
import com.foxminded.university.dto.VacationDto;
import com.foxminded.university.model.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VacationMapperTest {

    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private SubjectMapper subjectMapper = new SubjectMapperImpl(cathedraMapper);
    private TeacherMapper teacherMapper = new TeacherMapperImpl(cathedraMapper, subjectMapper);
    private VacationMapper vacationMapper = new VacationMapperImpl(teacherMapper);

    @Test
    void shouldMapVacationToVacationDto() {
        // given
        Cathedra cathedra = Cathedra.builder().id(1).build();
        Subject subject = Subject.builder().id(1).cathedra(cathedra).build();
        Teacher teacher = Teacher.builder().id(1).gender(Gender.MALE).cathedra(cathedra).subjects(Arrays.asList(subject)).degree(Degree.PROFESSOR).build();
        Vacation vacation = Vacation.builder()
                .id(1)
                .teacher(teacher)
                .start(LocalDate.of(2021, 1, 1))
                .end(LocalDate.of(2021, 1, 2))
                .build();
        // when
        VacationDto vacationDto = vacationMapper.vacationToDto(vacation);
        // then
        assertNotNull(vacationDto);
        assertEquals(vacationDto.getId(), 1);
        assertEquals(vacationDto.getStart(), LocalDate.of(2021, 1, 1));
        assertEquals(vacationDto.getEnd(), LocalDate.of(2021, 1, 2));
        assertEquals(vacationDto.getTeacher().getCathedra().getId(), 1);
        assertEquals(vacationDto.getTeacher().getSubjects().get(0).getId(), 1);
        assertEquals(vacationDto.getTeacher().getSubjects().get(0).getCathedra().getId(), 1);
    }

    @Test
    void shouldMapVacationDtoToVacation() {
        // given
        CathedraDto cathedraDto = new CathedraDto(1, "Fantastic Cathedra");
        SubjectDto subjectDto = new SubjectDto(1, cathedraDto, "Subject Name", "Subject desc" );
        TeacherDto teacherDto = new TeacherDto(1, "TestName", "TestLastName", "88005553535", "Address", "one@mail.com", Gender.MALE, "123", "Edu", LocalDate.of(2020,1,1), cathedraDto, Arrays.asList(subjectDto), Degree.PROFESSOR);
        VacationDto vacationDto = new VacationDto(1, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 2), teacherDto);
        // when
        Vacation vacation = vacationMapper.dtoToVacation(vacationDto);
        // then
        assertNotNull(vacation);
        assertEquals(vacation.getId(), 1);
        assertEquals(vacation.getStart(), LocalDate.of(2021, 1, 1));
        assertEquals(vacation.getEnd(), LocalDate.of(2021, 1, 2));
        assertEquals(vacation.getTeacher().getCathedra().getId(), 1);
        assertEquals(vacation.getTeacher().getSubjects().get(0).getId(), 1);
        assertEquals(vacation.getTeacher().getSubjects().get(0).getCathedra().getId(), 1);
    }
}
