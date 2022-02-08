package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.GroupController;
import com.foxminded.university.dao.mapper.GroupMapper;
import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/groups")
public class GroupRestController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;
    private final GroupMapper groupMapper;

    public GroupRestController(GroupService groupService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @GetMapping
    public Slice getAllGroups() {
        logger.debug("Show all groups");

        return new Slice(groupService.findAll().stream().map(groupMapper::groupToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public GroupDto showGroup(@PathVariable int id) {
        logger.debug("Show group with id {}", id);

        return groupMapper.groupToDto(groupService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody GroupDto groupDto) {
        Group group = groupService.save(groupMapper.dtoToGroup(groupDto));
        logger.debug("Create new group. Id {}", group.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(group.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public void update(@RequestBody GroupDto groupDto) {
        groupService.save(groupMapper.dtoToGroup(groupDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestBody GroupDto groupDto) {
        groupService.delete(groupMapper.dtoToGroup(groupDto));
    }
}