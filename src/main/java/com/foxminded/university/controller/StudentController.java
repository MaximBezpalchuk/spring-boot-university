package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Student;
import com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	private StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping
	public String all(Model model, Pageable pageable) {
		Page<Student> page = studentService.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		model.addAttribute("students", page);

		return "students/index";
	}

	@GetMapping("/{id}")
	public String showStudent(@PathVariable int id, Model model) {
		logger.debug("Show student page with id {}", id);
		model.addAttribute("student", studentService.findById(id));

		return "students/show";
	}
}
