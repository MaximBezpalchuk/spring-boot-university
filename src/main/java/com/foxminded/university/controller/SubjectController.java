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

import com.foxminded.university.model.Subject;
import com.foxminded.university.service.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

	private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

	private SubjectService subjectService;

	public SubjectController(SubjectService subjectService) {
		this.subjectService = subjectService;
	}

	@GetMapping
	public String all(Model model, Pageable pageable) {
		Page<Subject> page = subjectService.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		model.addAttribute("subjects", page);

		return "subjects/index";
	}

	@GetMapping("/{id}")
	public String showSubject(@PathVariable("id") int id, Model model) {
		logger.debug("Show subject page with id {}", id);
		model.addAttribute("subject", subjectService.findById(id));

		return "subjects/show";
	}
}
