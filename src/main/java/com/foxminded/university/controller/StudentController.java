package com.foxminded.university.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foxminded.university.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.StudentService;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	private StudentService studentService;
	private GroupService groupService;

	public StudentController(StudentService studentService, GroupService groupService) {
		this.studentService = studentService;
		this.groupService = groupService;
	}

	@GetMapping
	public String all(Model model, Pageable pageable) {
		logger.debug("Show index page");
		Page<Student> page = studentService.findAll(pageable);
		model.addAttribute("students", page);

		return "students/index";
	}

	@GetMapping("/{id}")
	public String showStudent(@PathVariable int id, Model model) {
		logger.debug("Show student page with id {}", id);
		model.addAttribute("student", studentService.findById(id));

		return "students/show";
	}

	@GetMapping("/new")
	public String newStudent(Student student, Model model) {
		logger.debug("Show create page");
		model.addAttribute("groups", groupService.findAll());

		return "students/new";
	}

	@PostMapping
	public String create(@ModelAttribute Student student, Model model) {
		if (student.getGroup() != null) {
			student.setGroup(groupService.findById(student.getGroup().getId()));
		}
		studentService.save(student);
		logger.debug("Create new student. Id {}", student.getId());

		return "redirect:/students";
	}

	@GetMapping("/{id}/edit")
	public String editStudent(@PathVariable int id, Model model) {
		model.addAttribute("student", studentService.findById(id));
		model.addAttribute("groups", groupService.findAll());
		logger.debug("Show edit student page");

		return "students/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute Student student, @PathVariable int id) {
		logger.debug("Update student with id {}", id);
		if (student.getGroup().getId() != 0) {
			student.setGroup(groupService.findById(student.getGroup().getId()));
		}
		studentService.save(student);

		return "redirect:/students";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable int id) {
		logger.debug("Delete student with id {}", id);
		studentService.deleteById(id);

		return "redirect:/students";
	}
}
