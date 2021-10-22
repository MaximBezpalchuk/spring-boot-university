package com.foxminded.university.controller;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@Controller
@RequestMapping("/teachers")
public class TeacherController {

	private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

	private TeacherService teacherService;
	private SubjectService subjectService;
	private CathedraService cathedraService;

	public TeacherController(TeacherService teacherService, SubjectService subjectService,
			CathedraService cathedraService) {
		this.teacherService = teacherService;
		this.subjectService = subjectService;
		this.cathedraService = cathedraService;
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

	@GetMapping("/new")
	public String newStudent(Teacher teacher, Model model) {
		logger.debug("Show create page");
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		model.addAttribute("subjectsAttribute", subjectService.findAll());

		return "teachers/new";
	}

	@PostMapping
	public String create(@ModelAttribute("teacher") Teacher teacher, Model model) {
		teacher.setCathedra(cathedraService.findById(teacher.getCathedra().getId()));
		teacher.setSubjects(teacher.getSubjects().stream().map(subject -> subjectService.findById(subject.getId()))
				.collect(Collectors.toList()));
		teacherService.save(teacher);
		logger.debug("Create new teacher. Id {}", teacher.getId());

		return "redirect:/teachers";
	}

	@GetMapping("/{id}/edit")
	public String editTeacher(@PathVariable("id") int id, Model model) {
		model.addAttribute("teacher", teacherService.findById(id));
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		model.addAttribute("subjectsAttribute", subjectService.findAll());
		logger.debug("Show edit teacher page");

		return "teachers/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("teacher") Teacher teacher, @PathVariable("id") int id) {
		logger.debug("Update teacher with id {}", id);
		teacher.setCathedra(cathedraService.findById(teacher.getCathedra().getId()));
		teacher.setSubjects(teacher.getSubjects().stream().map(subject -> subjectService.findById(subject.getId()))
				.collect(Collectors.toList()));
		teacherService.save(teacher);

		return "redirect:/teachers";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		logger.debug("Delete teacher with id {}", id);
		teacherService.deleteById(id);

		return "redirect:/teachers";
	}
}
