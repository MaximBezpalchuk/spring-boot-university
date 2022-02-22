package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.GroupMapper;
import com.foxminded.university.dto.GroupDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class GroupSystemTest {

    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11");
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GroupMapper groupMapper;

    @Test
    public void whenGetAllGroups_thenAllGroupsReturned() {
        Slice actual = restTemplate.getForObject("/api/groups/", Slice.class);
        Group group1 = createGroupNoId();
        group1.setId(1);
        Group group2 = createGroupNoId();
        group2.setId(2);
        group2.setName("Killers2");
        GroupDto group1Dto = groupMapper.groupToDto(group1);
        GroupDto group2Dto = groupMapper.groupToDto(group2);

        assertArrayEquals(new GroupDto[]{group1Dto, group2Dto}, objectMapper.convertValue(actual.getItems(), GroupDto[].class));
    }

    @Test
    public void whenGetOneGroup_thenOneGroupReturned() {
        GroupDto actual = restTemplate.getForObject("/api/groups/{id}", GroupDto.class, 1);
        Group group = createGroupNoId();
        group.setId(1);
        GroupDto expected = groupMapper.groupToDto(group);

        assertEquals(expected, actual);
    }

    @Test
    void whenFindNotExistingGroup_thenGroupNotFound() {
        ResponseEntity<String> groupResponse = restTemplate.getForEntity("/api/groups/{id}", String.class, 67);

        assertEquals(HttpStatus.NOT_FOUND, groupResponse.getStatusCode());
    }

    @Test
    public void whenSaveGroup_thenGroupSaved() throws Exception {
        Group group = createGroupNoId();
        group.setName("Any Name");
        GroupDto groupDto = groupMapper.groupToDto(group);
        ResponseEntity<String> groupResponse = restTemplate.postForEntity("/api/groups/", groupDto, String.class);

        assertEquals(HttpStatus.CREATED, groupResponse.getStatusCode());

        groupDto.setId(3);
        Slice actual = restTemplate.getForObject("/api/groups/", Slice.class);
        Group group1 = createGroupNoId();
        group1.setId(1);
        Group group2 = createGroupNoId();
        group2.setId(2);
        group2.setName("Killers2");
        GroupDto group1Dto = groupMapper.groupToDto(group1);
        GroupDto group2Dto = groupMapper.groupToDto(group2);

        assertArrayEquals(new GroupDto[]{group1Dto, group2Dto, groupDto}, objectMapper.convertValue(actual.getItems(), GroupDto[].class));
    }

    @Test
    public void whenEditGroup_thenGroupFound() {
        Group group = createGroupNoId();
        group.setId(1);
        group.setName("Test Name");
        GroupDto groupDto = groupMapper.groupToDto(group);
        HttpEntity<GroupDto> groupHttpEntity = new HttpEntity<>(groupDto);
        ResponseEntity<String> groupResponse = restTemplate.exchange("/api/groups/{id}?_method=patch", HttpMethod.POST, groupHttpEntity, String.class, 1);

        assertEquals(groupResponse.getStatusCode(), HttpStatus.OK);

        GroupDto updatedGroup = restTemplate.getForObject("/api/groups/{id}", GroupDto.class, 1);

        assertEquals(groupDto, updatedGroup);
    }

    @Test
    public void whenDeleteGroup_thenGroupDeleted() {
        restTemplate.delete("/api/groups/{id}", 2);
        ResponseEntity<String> groupResponse = restTemplate.getForEntity("/groups/{id}", String.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, groupResponse.getStatusCode());
    }

    private Group createGroupNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();

        return Group.builder().name("Killers").cathedra(cathedra).build();
    }
}
