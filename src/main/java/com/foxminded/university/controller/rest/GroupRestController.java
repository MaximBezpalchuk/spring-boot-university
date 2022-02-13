package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.GroupController;
import com.foxminded.university.dao.mapper.GroupMapper;
import com.foxminded.university.dto.AudienceDto;
import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get all groups")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Show all groups",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Slice.class)) }),
        @ApiResponse(responseCode = "404", description = "Groups not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public Slice getAllGroups() {
        logger.debug("Show all groups");

        return new Slice(groupService.findAll().stream().map(groupMapper::groupToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a group by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the group",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = GroupDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Group not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public GroupDto showGroup(@Parameter(description = "Id of group to be searched") @PathVariable int id) {
        logger.debug("Show group with id {}", id);

        return groupMapper.groupToDto(groupService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new group by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Group successfully created",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public ResponseEntity create(@RequestBody GroupDto groupDto) {
        Group group = groupService.save(groupMapper.dtoToGroup(groupDto));
        logger.debug("Create new group. Id {}", group.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(group.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing group by its DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Group successfully updated", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Group not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void update(@RequestBody GroupDto groupDto) {
        groupService.save(groupMapper.dtoToGroup(groupDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing group by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Group successfully deleted", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Group not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal error", content = @Content)})
    public void delete(@Parameter(description = "Id of group to be deleted") @PathVariable int id) {
        groupService.delete(id);
    }
}