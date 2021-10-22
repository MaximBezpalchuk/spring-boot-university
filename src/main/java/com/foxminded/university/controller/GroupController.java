package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.service.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupController {

	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

	private GroupService groupService;

	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}

	@GetMapping
	public String getAllGroups(Model model) {
		logger.debug("Show index page");
		model.addAttribute("groups", groupService.findAll());

		return "groups/index";
	}

	@GetMapping("/{id}")
	public String showGroup(@PathVariable int id, Model model) {
		logger.debug("Show group page with id {}", id);
		model.addAttribute("group", groupService.findById(id));

		return "groups/show";
	}
}
