package com.foxminded.university.mapper;

import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dao.mapper.GroupMapper;
import com.foxminded.university.dao.mapper.GroupMapperImpl;
import com.foxminded.university.dto.CathedraDto;
import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupMapperTest {

    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private GroupMapper groupMapper = new GroupMapperImpl(cathedraMapper);

    @Test
    void shouldMapGroupToGroupDto() {
        // given
        Cathedra cathedra = Cathedra.builder().id(1).build();
        Group group = Group.builder().id(1).name("Killers").cathedra(cathedra).build();
        // when
        GroupDto groupDto = groupMapper.groupToDto(group);
        // then
        assertNotNull(groupDto);
        assertEquals(groupDto.getId(), 1);
        assertEquals(groupDto.getName(), "Killers");
        assertEquals(groupDto.getCathedra().getId(), 1);
    }

    @Test
    void shouldMapGroupDtoToGroup() {
        // given
        CathedraDto cathedraDto = new CathedraDto(1, "Name");
        GroupDto groupDto = new GroupDto(1, "Killers", cathedraDto);
        // when
        Group group = groupMapper.dtoToGroup(groupDto);
        // then
        assertNotNull(group);
        assertEquals(group.getId(), 1);
        assertEquals(group.getName(), "Killers");
        assertEquals(group.getCathedra().getId(), 1);
    }
}
