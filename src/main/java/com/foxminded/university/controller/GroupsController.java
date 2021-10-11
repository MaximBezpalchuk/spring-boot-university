package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Group;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

	@Autowired
	GroupService groupService;
	@Autowired
	CathedraService cathedraService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("groups", groupService.findAll());

		return "groups/index";
	}

	@GetMapping("/{id}")
	public String showGroup(@PathVariable("id") int id, Model model) {
		model.addAttribute("group", groupService.findById(id));

		return "groups/show";
	}
	
	@GetMapping("/new")
	public String newGroup(Group group, Model model) {
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());

		return "groups/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("group") Group group, Model model) {
		group.setCathedra(cathedraService.findById(group.getCathedra().getId()));
		groupService.save(group);

		return "redirect:/groups";
	}
	
	@GetMapping("/{id}/edit")
	public String editGroup(@PathVariable("id") int id, Model model) {
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		model.addAttribute("group", groupService.findById(id));

		return "groups/edit";
	}
	
	@PatchMapping("/{id}")
	public String update(@ModelAttribute("group") Group group, @PathVariable("id") int id) {
		group.setCathedra(cathedraService.findById(group.getCathedra().getId()));
		groupService.save(group);
		
		return"redirect:/groups";
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		groupService.deleteById(id);
		
		return"redirect:/groups";
	}
}
