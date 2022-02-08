package com.foxminded.university.dao.mapper;

import com.foxminded.university.model.Group;
import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.service.CathedraService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class GroupMapper {

    public static GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);
    @Autowired
    protected CathedraService cathedraService;

    @Mapping(target = "id", source = "group.id")
    @Mapping(target = "name", source = "group.name")
    @Mapping(target = "cathedraName", source = "group.cathedra.name")
    public abstract GroupDto groupToDto(Group group);

    @Mapping(target = "id", source = "groupDto.id")
    @Mapping(target = "name", source = "groupDto.name")
    @Mapping(target = "cathedra", expression = "java(cathedraService.findByName(groupDto.getCathedraName()))")
    public abstract Group dtoToGroup(GroupDto groupDto);
}
