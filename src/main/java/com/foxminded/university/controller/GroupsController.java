package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

	@Autowired
	GroupService groupService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("groups", groupService.findAll());
		return "groups/index";
	}
	
	@GetMapping("/new")
	public String newGroup(Model model) {
		model.addAttribute("group", Group.builder().build());
		return "groups/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("group") Group group, Model model) {
		group.setCathedra(Cathedra.builder().id(1).build());
		groupService.save(group);
		return "redirect:/groups";
	}
}
