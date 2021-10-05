package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;

@Controller
@RequestMapping("/lectures")
public class LecturesController {

	@Autowired
	LectureService lectureService;
	
	@Autowired
	GroupService groupService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("lectures", lectureService.findAll());
		return "lectures/index";
	}
	
	@GetMapping("/new")
	public String newLecture(Model model) {
		model.addAttribute("groupsAttribute", groupService.findAll());
		model.addAttribute("lecture", Lecture.builder().build());
		return "lectures/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("lecture") Lecture lecture, Model model) {
		lecture.setCathedra(Cathedra.builder().id(1).build());
		lectureService.save(lecture);
		return "redirect:/lectures";
	}
}
