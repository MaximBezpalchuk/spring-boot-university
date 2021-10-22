package com.foxminded.university.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.GroupService;

@ExtendWith(MockitoExtension.class)
public class GroupControllerTest {

	private MockMvc mockMvc;

	@Mock
	private GroupService groupService;
	@Mock
	private CathedraService cathedraService;
	@InjectMocks
	private GroupController groupController;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
	}

	@Test
	public void whenGetAllGroups_thenAllGroupsReturned() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Group group1 = Group.builder().id(1).name("Killers").cathedra(cathedra).build();
		Group group2 = Group.builder()
				.id(2)
				.name("Mages")
				.cathedra(cathedra)
				.build();
		List<Group> groups = Arrays.asList(group1, group2);

		when(groupService.findAll()).thenReturn(groups);

		mockMvc.perform(get("/groups"))
				.andExpect(status().isOk())
				.andExpect(view().name("groups/index"))
				.andExpect(forwardedUrl("groups/index"))
				.andExpect(model().attribute("groups", groups));

		verifyNoMoreInteractions(groupService);
	}

	@Test
	public void whenGetOneGroup_thenOneGroupReturned() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Group group = Group.builder().id(1).name("Killers").cathedra(cathedra).build();

		when(groupService.findById(group.getId())).thenReturn(group);

		mockMvc.perform(get("/groups/{id}", group.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name("groups/show"))
				.andExpect(forwardedUrl("groups/show"))
				.andExpect(model().attribute("group", group));
	}
}