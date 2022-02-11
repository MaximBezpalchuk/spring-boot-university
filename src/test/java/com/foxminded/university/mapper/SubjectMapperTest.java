package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dao.mapper.SubjectMapper;
import com.foxminded.university.dao.mapper.SubjectMapperImpl;
import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubjectMapperTest {

    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private SubjectMapper subjectMapper = new SubjectMapperImpl(cathedraMapper);

    @Test
    void shouldMapSubjectToSubjectDto() {
        // given
        Cathedra cathedra = Cathedra.builder().id(1).build();
        Subject subject = Subject.builder()
                .id(1)
                .name("Subject Name")
                .description("Subject desc")
                .cathedra(cathedra)
                .build();
        // when
        SubjectDto subjectDto = subjectMapper.subjectToDto(subject);
        // then
        assertNotNull(subjectDto);
        assertEquals(subjectDto.getId(), 1);
        assertEquals(subjectDto.getName(), "Subject Name");
        assertEquals(subjectDto.getDescription(), "Subject desc");
        assertEquals(subjectDto.getCathedra().getId(), 1);
    }

    @Test
    void shouldMapSubjectDtoToSubject() {
        // given
        CathedraDto cathedraDto = new CathedraDto(1, "Fantastic Cathedra");
        SubjectDto subjectDto = new SubjectDto(1, cathedraDto, "Subject Name", "Subject desc" );
        // when
        Subject subject = subjectMapper.dtoToSubject(subjectDto);
        // then
        assertNotNull(subject);
        assertEquals(subject.getId(), 1);
        assertEquals(subject.getName(), "Subject Name");
        assertEquals(subject.getDescription(), "Subject desc");
        assertEquals(subject.getCathedra().getId(), 1);
    }
}
