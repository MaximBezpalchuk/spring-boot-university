package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.GroupController;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/groups")
public class RestGroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;

    public RestGroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<Group> getAllGroups() {
        logger.debug("Show all groups");

        return groupService.findAll();
    }

    @GetMapping("/{id}")
    public Group showGroup(@PathVariable int id) {
        logger.debug("Show group with id {}", id);

        return groupService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Group group) {
        logger.debug("Create new group. Id {}", group.getId());
        groupService.save(group);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Group group, @PathVariable int id) {
        logger.debug("Update group with id {}", id);
        groupService.save(group);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@ModelAttribute Group group) {
        logger.debug("Delete group with id {}", group.getId());
        groupService.delete(group);
    }
}