package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Subject;
import com.foxminded.university.service.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectsController {

	@Autowired
	SubjectService subjectService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("subjects", subjectService.findAll());
		return "subjects/index";
	}

	@GetMapping("/new")
	public String newSubject(Model model) {
		model.addAttribute("subject", Subject.builder().build());
		return "subjects/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("subject") Subject subject, Model model) {
		subject.setCathedra(Cathedra.builder().id(1).build());
		subjectService.save(subject);
		return "redirect:/subjects";
	}
}
