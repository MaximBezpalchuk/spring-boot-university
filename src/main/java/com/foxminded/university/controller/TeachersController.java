package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeachersController {

	@Autowired
	TeacherService teacherService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("teachers", teacherService.findAll());
		return "teachers/index";
	}
}
