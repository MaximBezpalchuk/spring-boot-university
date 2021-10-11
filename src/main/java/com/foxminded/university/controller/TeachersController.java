package com.foxminded.university.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;

@Controller
@RequestMapping("/teachers")
public class TeachersController {

	@Autowired
	TeacherService teacherService;
	@Autowired
	SubjectService subjectService;
	@Autowired
	CathedraService cathedraService;
	@Autowired
	VacationService vacationService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("teachers", teacherService.findAll());

		return "teachers/index";
	}

	@GetMapping("/{id}")
	public String showTeacher(@PathVariable("id") int id, Model model) {
		model.addAttribute("teacher", teacherService.findById(id));

		return "teachers/show";
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

	@GetMapping("/{id}/edit")
	public String editTeacher(@PathVariable("id") int id, Model model) {
		model.addAttribute("teacher", teacherService.findById(id));
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		model.addAttribute("subjectsAttribute", subjectService.findAll());

		return "teachers/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("teacher") Teacher teacher, @PathVariable("id") int id) {
		teacher.setCathedra(cathedraService.findById(teacher.getCathedra().getId()));
		teacher.setSubjects(teacher.getSubjects().stream().map(subject -> subjectService.findById(subject.getId()))
				.collect(Collectors.toList()));
		teacherService.save(teacher);

		return "redirect:/teachers";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		teacherService.deleteById(id);

		return "redirect:/teachers";
	}
}
