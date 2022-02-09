package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.CathedraService;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GroupMapper {

    @Mapping(target = "cathedraDto", source = "group.cathedra")
    GroupDto groupToDto(Group group);

    @Mapping(target = "cathedra", source = "groupDto.cathedraDto")
    Group dtoToGroup(GroupDto groupDto);
}
