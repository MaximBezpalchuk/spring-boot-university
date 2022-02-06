package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.GroupController;
import com.foxminded.university.dao.mapper.GroupDtoMapper;
import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.dto.ObjectListDto;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/groups")
public class GroupRestController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;
    private final GroupDtoMapper groupDtoMapper;

    public GroupRestController(GroupService groupService, GroupDtoMapper groupDtoMapper) {
        this.groupService = groupService;
        this.groupDtoMapper = groupDtoMapper;
    }

    @GetMapping
    public ObjectListDto getAllGroups() {
        logger.debug("Show all groups");

        return new ObjectListDto(groupService.findAll().stream().map(groupDtoMapper::groupToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public GroupDto showGroup(@PathVariable int id) {
        logger.debug("Show group with id {}", id);

        return groupDtoMapper.groupToDto(groupService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody GroupDto groupDto) {
        Group group = groupService.save(groupDtoMapper.dtoToGroup(groupDto));
        logger.debug("Create new group. Id {}", group.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(group.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public void update(@RequestBody GroupDto groupDto) {
        groupService.save(groupDtoMapper.dtoToGroup(groupDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestBody GroupDto groupDto) {
        groupService.delete(groupDtoMapper.dtoToGroup(groupDto));
    }
}