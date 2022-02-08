package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.GroupMapper;
import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.dto.ObjectListDto;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GroupRestControllerTest {

    private MockMvc mockMvc;
    private final GroupMapper groupMapper = GroupMapper.INSTANCE;
    ObjectMapper objectMapper;
    @Mock
    private GroupService groupService;
    @Mock
    private CathedraService cathedraService;
    @InjectMocks
    private GroupRestController groupRestController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupRestController).build();
        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(groupRestController, "groupMapper", groupMapper);
        ReflectionTestUtils.setField(groupMapper, "cathedraService", cathedraService);
    }

    @Test
    public void whenGetAllGroups_thenAllGroupsReturned() throws Exception {
        Group group1 = createGroupNoId();
        group1.setId(1);
        Group group2 = createGroupNoId();
        group2.setId(2);
        List<Group> groups = Arrays.asList(group1, group2);

        when(groupService.findAll()).thenReturn(groups);

        mockMvc.perform(get("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ObjectListDto(groups))))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void whenGetOneGroup_thenOneGroupReturned() throws Exception {
        Group group = createGroupNoId();
        group.setId(1);

        when(groupService.findById(group.getId())).thenReturn(group);

        mockMvc.perform(get("/api/groups/{id}", group.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupMapper.groupToDto(group))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveGroup_thenGroupSaved() throws Exception {
        Group group1 = createGroupNoId();
        Group group2 = createGroupNoId();
        group2.setId(3);
        GroupDto groupDto = groupMapper.groupToDto(group1);
        when(cathedraService.findByName(groupDto.getCathedraName())).thenReturn(group1.getCathedra());
        when(groupService.save(group1)).thenReturn(group2);
        mockMvc.perform(post("/api/groups")
                .content(objectMapper.writeValueAsString(groupDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/groups/3"))
                .andExpect(status().isCreated());

        verify(groupService).save(group1);
    }

    @Test
    public void whenEditGroup_thenGroupFound() throws Exception {
        Group group = createGroupNoId();
        group.setId(1);
        GroupDto groupDto = groupMapper.groupToDto(group);
        when(cathedraService.findByName(groupDto.getCathedraName())).thenReturn(group.getCathedra());
        when(groupService.save(group)).thenReturn(group);

        mockMvc.perform(patch("/api/groups/{id}", 1)
                .content(objectMapper.writeValueAsString(groupDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteGroup_thenGroupDeleted() throws Exception {
        Group group = createGroupNoId();
        group.setId(1);
        GroupDto groupDto = groupMapper.groupToDto(group);
        when(cathedraService.findByName(groupDto.getCathedraName())).thenReturn(group.getCathedra());
        mockMvc.perform(delete("/api/groups/{id}", 1)
                .content(objectMapper.writeValueAsString(groupDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(groupService).delete(group);
    }

    private Group createGroupNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Group.builder().name("Killers").cathedra(cathedra).build();
    }
}