package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Subject;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectsController {

	@Autowired
	SubjectService subjectService;
	@Autowired
	CathedraService cathedraService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("subjects", subjectService.findAll());

		return "subjects/index";
	}
	
	@GetMapping("/{id}")
	public String showSubject(@PathVariable("id") int id, Model model) {
		model.addAttribute("subject", subjectService.findById(id));

		return "subjects/show";
	}

	@GetMapping("/new")
	public String newSubject(Subject subject, Model model) {
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());

		return "subjects/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("subject") Subject subject, Model model) {
		subject.setCathedra(cathedraService.findById(subject.getCathedra().getId()));
		subjectService.save(subject);

		return "redirect:/subjects";
	}
}
