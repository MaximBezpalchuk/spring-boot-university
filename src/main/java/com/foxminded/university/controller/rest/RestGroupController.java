package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.GroupController;
import com.foxminded.university.dao.mapper.GroupDtoMapper;
import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/groups")
public class RestGroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;
    private final GroupDtoMapper groupDtoMapper;

    public RestGroupController(GroupService groupService, GroupDtoMapper groupDtoMapper) {
        this.groupService = groupService;
        this.groupDtoMapper = groupDtoMapper;
    }

    @GetMapping
    public List<GroupDto> getAllGroups() {
        logger.debug("Show all groups");

        return groupService.findAll().stream().map(groupDtoMapper::groupToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GroupDto showGroup(@PathVariable int id) {
        logger.debug("Show group with id {}", id);

        return groupDtoMapper.groupToDto(groupService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody GroupDto groupDto) {
        //logger.debug("Create new group. Id {}", group.getId());
        groupService.save(groupDtoMapper.dtoToGroup(groupDto));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody GroupDto groupDto, @PathVariable int id) {
        //logger.debug("Update group with id {}", id);
        groupService.save(groupDtoMapper.dtoToGroup(groupDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody GroupDto groupDto) {
        //logger.debug("Delete group with id {}", group.getId());
        groupService.delete(groupDtoMapper.dtoToGroup(groupDto));
    }
}