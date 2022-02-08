package com.foxminded.university.dao.mapper;

import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.CathedraService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class GroupMapper {

    @Autowired
    protected CathedraService cathedraService;
    @Autowired
    protected CathedraMapper cathedraMapper;

    @Mapping(target = "cathedraDto", expression = "java(cathedraMapper.cathedraToDto(group.getCathedra()))")
    public abstract GroupDto groupToDto(Group group);

    @Mapping(target = "cathedra", expression = "java(cathedraMapper.dtoToCathedra(groupDto.getCathedraDto()))")
    public abstract Group dtoToGroup(GroupDto groupDto);
}
