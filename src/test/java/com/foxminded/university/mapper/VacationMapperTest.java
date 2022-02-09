package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.*;
import com.foxminded.university.dto.VacationDto;
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
        assertEquals(vacationDto.getTeacherDto().getCathedraDto().getId(), 1);
        assertEquals(vacationDto.getTeacherDto().getSubjectDtos().get(0).getId(), 1);
        assertEquals(vacationDto.getTeacherDto().getSubjectDtos().get(0).getCathedraDto().getId(), 1);
    }
}
