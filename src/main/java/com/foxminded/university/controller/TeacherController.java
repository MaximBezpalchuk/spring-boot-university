package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

	private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

	private TeacherService teacherService;

	public TeacherController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@GetMapping
	public String all(Model model, Pageable pageable) {
		logger.debug("Show index page");
		Page<Teacher> page = teacherService.findAll(pageable);
		model.addAttribute("teachers", page);

		return "teachers/index";
	}

	@GetMapping("/{id}")
	public String showTeacher(@PathVariable int id, Model model) {
		logger.debug("Show teacher page with id {}", id);
		model.addAttribute("teacher", teacherService.findById(id));

		return "teachers/show";
	}
}
