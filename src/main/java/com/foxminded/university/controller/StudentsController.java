package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentsController {

	@Autowired
	StudentService studentService;
	@Autowired
	GroupService groupService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("students", studentService.findAll());

		return "students/index";
	}
	
	@GetMapping("/{id}")
	public String showStudent(@PathVariable("id") int id, Model model) {
		model.addAttribute("student", studentService.findById(id));

		return "students/show";
	}

	@GetMapping("/new")
	public String newStudent(Student student, Model model) {
		model.addAttribute("groupsAttribute", groupService.findAll());

		return "students/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("student") Student student, Model model) {
		if (student.getGroup().getId() != 0) {
			student.setGroup(groupService.findById(student.getGroup().getId()));
		}
		studentService.save(student);

		return "redirect:/students";
	}
}