package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestGroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;
    @InjectMocks
    private RestGroupController restGroupController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restGroupController).build();
    }

    @Test
    public void whenGetAllGroups_thenAllGroupsReturned() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Group group1 = Group.builder().id(1).name("Killers").cathedra(cathedra).build();
        Group group2 = Group.builder().id(2).name("Mages").cathedra(cathedra).build();
        List<Group> groups = Arrays.asList(group1, group2);

        when(groupService.findAll()).thenReturn(groups);

        mockMvc.perform(get("/api/groups"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Killers")))
            .andExpect(jsonPath("$[0].cathedra.id", is(1)))
            .andExpect(jsonPath("$[0].cathedra.name", is("Fantastic Cathedra")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].name", is("Mages")))
            .andExpect(jsonPath("$[1].cathedra.id", is(1)))
            .andExpect(jsonPath("$[1].cathedra.name", is("Fantastic Cathedra")));

        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void whenGetOneGroup_thenOneGroupReturned() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Group group = Group.builder().id(1).name("Killers").cathedra(cathedra).build();

        when(groupService.findById(group.getId())).thenReturn(group);

        mockMvc.perform(get("/api/groups/{id}", group.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Killers")))
            .andExpect(jsonPath("$.cathedra.id", is(1)))
            .andExpect(jsonPath("$.cathedra.name", is("Fantastic Cathedra")));
    }

    @Test
    void whenSaveGroup_thenGroupSaved() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Group group = Group.builder()
            .name("Killers2")
            .cathedra(cathedra)
            .build();
        mockMvc.perform(post("/api/groups")
            .content(new ObjectMapper().writeValueAsBytes(group))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(groupService).save(group);
    }

    @Test
    void whenEditGroup_thenGroupFound() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Group actual = Group.builder()
            .id(1)
            .name("Killers")
            .cathedra(cathedra)
            .build();

        mockMvc.perform(patch("/api/groups/{id}", 1)
            .content(new ObjectMapper().writeValueAsBytes(actual))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void whenDeleteGroup_thenGroupDeleted() throws Exception {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Group group = Group.builder()
            .id(1)
            .name("Killers")
            .cathedra(cathedra)
            .build();
        mockMvc.perform(delete("/api/groups/{id}", 1)
            .content(new ObjectMapper().writeValueAsBytes(group))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(groupService).delete(Group.builder().id(1).build());
    }
}