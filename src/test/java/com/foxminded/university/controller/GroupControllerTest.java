package com.foxminded.university.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }
	
	@Test
    public void whenGetAllGroups_thenAllGroupsReturned() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Group first = Group.builder().id(1).name("Killers").cathedra(cathedra).build();
		Group second = Group.builder()
				.id(2)
				.name("Mages")
				.cathedra(cathedra)
				.build();
		List<Group> list = Arrays.asList(first, second);
		
        when(groupService.findAll()).thenReturn(list);
 
        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/index"))
                .andExpect(forwardedUrl("groups/index"))
                .andExpect(model().attribute("groups", hasSize(2)))
                .andExpect(model().attribute("groups", list));
 
        verify(groupService, times(1)).findAll();
        verifyNoMoreInteractions(groupService);
    }
	
	@Test
	void whenCreateNewGroup_thenNewGroupCreated() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		
		when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
		
		mockMvc.perform(get("/groups/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("groups/new"))
				.andExpect(forwardedUrl("groups/new"))
				.andExpect(model().attribute("group", instanceOf(Group.class)));
	}
	
	@Test
	void whenSaveGroup_thenGroupSaved() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Group group = Group.builder()
				.name("Killers2")
				.cathedra(cathedra)
				.build();
		mockMvc.perform(post("/groups").flashAttr("group", group))		
				.andExpect(redirectedUrl("/groups"));
		
		verify(groupService).save(group);
	}

	@Test
	void whenEditGroup_thenGroupFound() throws Exception {
		Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
		Group expected = Group.builder()
				.id(1)
				.name("Killers")
				.cathedra(cathedra)
				.build();
		
		when(groupService.findById(1)).thenReturn(expected);
		when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
		
		mockMvc.perform(get("/groups/{id}/edit", 1))
		.andExpect(status().isOk())
		.andExpect(view().name("groups/edit"))
		.andExpect(forwardedUrl("groups/edit"))
		.andExpect(model().attribute("group", is(expected)));
	}
	
	@Test
	void whenDeleteGroup_thenGroupDeleted() throws Exception {
		mockMvc.perform(delete("/groups/{id}", 1))
				.andExpect(redirectedUrl("/groups"));
		
		verify(groupService).deleteById(1);
	}
}