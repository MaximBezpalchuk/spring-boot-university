package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.CathedraMapper;
import com.foxminded.university.dao.mapper.GroupMapper;
import com.foxminded.university.dao.mapper.GroupMapperImpl;
import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class GroupSystemTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private CathedraMapper cathedraMapper = Mappers.getMapper(CathedraMapper.class);
    private GroupMapper groupMapper = new GroupMapperImpl(cathedraMapper);

    @Test
    public void whenGetAllGroups_thenAllGroupsReturned() throws Exception {
        Group group1 = createGroupNoId();
        group1.setId(1);
        Group group2 = createGroupNoId();
        group2.setId(2);
        group2.setName("Killers2");
        List<Group> groups = Arrays.asList(group1, group2);
        List<GroupDto> groupDtos = groups.stream().map(groupMapper::groupToDto).collect(Collectors.toList());

        mockMvc.perform(get("/api/groups")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(new Slice(groupDtos))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetOneGroup_thenOneGroupReturned() throws Exception {
        Group group = createGroupNoId();
        group.setId(1);
        GroupDto groupDto = groupMapper.groupToDto(group);

        mockMvc.perform(get("/api/groups/{id}", group.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(groupDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveGroup_thenGroupSaved() throws Exception {
        Group group = createGroupNoId();
        group.setName("Any Name");
        GroupDto groupDto = groupMapper.groupToDto(group);

        mockMvc.perform(post("/api/groups")
                .content(objectMapper.writeValueAsString(groupDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/groups/3"))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenEditGroup_thenGroupFound() throws Exception {
        Group group = createGroupNoId();
        group.setId(1);
        GroupDto groupDto = groupMapper.groupToDto(group);

        mockMvc.perform(patch("/api/groups/{id}", 1)
                .content(objectMapper.writeValueAsString(groupDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteGroup_thenGroupDeleted() throws Exception {
        mockMvc.perform(delete("/api/groups/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private Group createGroupNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Group.builder().name("Killers").cathedra(cathedra).build();
    }
}
