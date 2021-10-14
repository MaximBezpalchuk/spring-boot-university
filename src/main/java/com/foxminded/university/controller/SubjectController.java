package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Subject;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

	private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

	private SubjectService subjectService;
	private CathedraService cathedraService;

	public SubjectController(SubjectService subjectService, CathedraService cathedraService) {
		this.subjectService = subjectService;
		this.cathedraService = cathedraService;
	}

	@GetMapping()
	public String index(Model model) {
		logger.debug("Show index page");
		model.addAttribute("subjects", subjectService.findAll());

		return "subjects/index";
	}

	@GetMapping("/{id}")
	public String showSubject(@PathVariable("id") int id, Model model) {
		logger.debug("Show subject page with id {}", id);
		model.addAttribute("subject", subjectService.findById(id));

		return "subjects/show";
	}

	@GetMapping("/new")
	public String newSubject(Subject subject, Model model) {
		logger.debug("Show create page");
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());

		return "subjects/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("subject") Subject subject, Model model) {
		subject.setCathedra(cathedraService.findById(subject.getCathedra().getId()));
		subjectService.save(subject);
		logger.debug("Create new subject. Id {}", subject.getId());

		return "redirect:/subjects";
	}

	@GetMapping("/{id}/edit")
	public String editSubject(@PathVariable("id") int id, Model model) {
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		model.addAttribute("subject", subjectService.findById(id));
		logger.debug("Show edit subject page");

		return "subjects/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("subject") Subject subject, @PathVariable("id") int id) {
		logger.debug("Update subject with id {}", id);
		subject.setCathedra(cathedraService.findById(subject.getCathedra().getId()));
		subjectService.save(subject);

		return "redirect:/subjects";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		logger.debug("Delete subject with id {}", id);
		subjectService.deleteById(id);

		return "redirect:/subjects";
	}
}
