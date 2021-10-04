package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Student;
import com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentsController {

	@Autowired
	StudentService studentService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("students", studentService.findAll());
		return "students/index";
	}

	@GetMapping("/new")
	public String newStudent(Model model) {
		model.addAttribute("student", Student.builder().build());
		return "students/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("student") Student student) {
		studentService.save(student);
		return "redirect:/students";
	}
}
