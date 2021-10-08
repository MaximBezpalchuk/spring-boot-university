package com.foxminded.university.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeachersController {

	@Autowired
	TeacherService teacherService;
	@Autowired
	SubjectService subjectService;
	@Autowired
	CathedraService cathedraService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("teachers", teacherService.findAll());

		return "teachers/index";
	}

	@GetMapping("/new")
	public String newStudent(Teacher teacher, Model model) {
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		model.addAttribute("subjectsAttribute", subjectService.findAll());

		return "teachers/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("teacher") Teacher teacher, Model model) {
		teacher.setCathedra(cathedraService.findById(teacher.getCathedra().getId()));
		teacher.setSubjects(teacher.getSubjects().stream().map(subject -> subjectService.findById(subject.getId()))
				.collect(Collectors.toList()));
		teacherService.save(teacher);

		return "redirect:/teachers";
	}
}
