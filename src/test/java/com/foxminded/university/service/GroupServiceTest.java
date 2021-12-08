package com.foxminded.university.service;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.EntityNotUniqueException;
import com.foxminded.university.model.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private GroupDao groupDao;
    @InjectMocks
    private GroupService groupService;

    @Test
    void givenListOfGroups_whenFindAll_thenAllExistingGroupsFound() {
        Group group1 = Group.builder().id(1).build();
        Group group2 = Group.builder().id(2).build();
        List<Group> expected = Arrays.asList(group1, group2);
        when(groupDao.findAll()).thenReturn(expected);
        List<Group> actual = groupService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenExistingGroup_whenFindById_thenGroupFound() {
        Optional<Group> expected = Optional.of(Group.builder().id(1).build());
        when(groupDao.findById(1)).thenReturn(expected);
        Group actual = groupService.findById(1);

        assertEquals(expected.get(), actual);
    }

    @Test
    void givenExistingGroup_whenFindById_thenEntityNotFoundException() {
        when(groupDao.findById(10)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            groupService.findById(10);
        });

        assertEquals("Can`t find any group with id: 10", exception.getMessage());
    }

    @Test
    void givenNewGroup_whenSave_thenSaved() throws Exception {
        Group group = Group.builder().build();
        groupService.save(group);

        verify(groupDao).save(group);
    }

    @Test
    void givenExistingGroup_whenSave_thenSaved() {
        Group group = Group.builder()
                .name("TestName")
                .build();
        when(groupDao.findByName(group.getName())).thenReturn(Optional.of(group));
        groupService.save(group);

        verify(groupDao).save(group);
    }

    @Test
    void givenExistingGroupId_whenDelete_thenDeleted() {
        Group group = Group.builder().id(1).build();
        groupService.delete(group);

        verify(groupDao).delete(group);
    }

    @Test
    void givenNotUniqueGroup_whenSave_thenEntityNotUniqueException() {
        Group group1 = Group.builder().id(1).name("Test1").build();
        Group group2 = Group.builder().id(2).name("Test2").build();
        when(groupDao.findByName(group1.getName())).thenReturn(Optional.of(group2));
        Exception exception = assertThrows(EntityNotUniqueException.class, () -> {
            groupService.save(group1);
        });

        assertEquals("Group with name Test1 is already exists!", exception.getMessage());
    }
}
