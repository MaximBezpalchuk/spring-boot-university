package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.model.Group;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GroupMapper {

    GroupDto groupToDto(Group group);

    Group dtoToGroup(GroupDto groupDto);
}
