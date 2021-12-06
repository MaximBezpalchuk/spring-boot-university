package com.foxminded.university.controller;

import com.foxminded.university.model.Group;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
public class GroupController {

	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

	private GroupService groupService;
	private CathedraService cathedraService;

	public GroupController(GroupService groupService, CathedraService cathedraService) {
		this.groupService = groupService;
		this.cathedraService = cathedraService;
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

	@GetMapping("/new")
	public String newGroup(Group group, Model model) {
		logger.debug("Show create page");
		model.addAttribute("cathedras", cathedraService.findAll());

		return "groups/new";
	}

	@PostMapping
	public String create(@ModelAttribute Group group, Model model) {
		group.setCathedra(cathedraService.findById(group.getCathedra().getId()));
		groupService.save(group);
		logger.debug("Create new group. Id {}", group.getId());

		return "redirect:/groups";
	}

	@GetMapping("/{id}/edit")
	public String editGroup(@PathVariable int id, Model model) {
		model.addAttribute("cathedras", cathedraService.findAll());
		model.addAttribute("group", groupService.findById(id));
		logger.debug("Show edit group page");

		return "groups/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute Group group, @PathVariable int id) {
		logger.debug("Update group with id {}", id);
		group.setCathedra(cathedraService.findById(group.getCathedra().getId()));
		groupService.save(group);

		return "redirect:/groups";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable int id) {
		logger.debug("Delete group with id {}", id);
		groupService.deleteById(id);

		return "redirect:/groups";
	}
}
