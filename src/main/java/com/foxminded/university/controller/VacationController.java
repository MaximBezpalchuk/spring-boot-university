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

import com.foxminded.university.model.Vacation;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;

@Controller
@RequestMapping("/teachers/{id}/vacations")
public class VacationController {

	private static final Logger logger = LoggerFactory.getLogger(VacationController.class);

	private TeacherService teacherService;
	private VacationService vacationService;

	public VacationController(TeacherService teacherService, VacationService vacationService) {
		this.teacherService = teacherService;
		this.vacationService = vacationService;
	}

	@GetMapping
	public String all(@PathVariable("id") int id, Model model, Pageable pageable) {
		Page<Vacation> page = vacationService
				.findByTeacherId(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), id);
		model.addAttribute("vacations", page);
		model.addAttribute("teacher", teacherService.findById(id));

		return "teachers/vacations/index";
	}

	@GetMapping("/{vacationId}")
	public String showVacation(@PathVariable("vacationId") int id, Model model) {
		logger.debug("Show vacation page with id {}", id);
		model.addAttribute("vacation", vacationService.findById(id));

		return "teachers/vacations/show";
	}
}
