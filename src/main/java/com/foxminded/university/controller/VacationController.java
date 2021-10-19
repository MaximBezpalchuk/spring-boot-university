package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

	@GetMapping("/new")
	public String newTeacherVacations(@PathVariable("id") int id, Vacation vacation, Model model) {
		logger.debug("Show create page");
		model.addAttribute("teacher", teacherService.findById(id));

		return "teachers/vacations/new";
	}

	@PostMapping
	public String createVacation(@PathVariable("id") int id, @ModelAttribute("vacation") Vacation vacation,
			Model model) {
		vacation.setTeacher(teacherService.findById(id));
		vacationService.save(vacation);
		logger.debug("Create new vacation. Id {}", vacation.getId());

		return "redirect:/teachers/" + id + "/vacations";
	}

	@GetMapping("/{vacationId}/edit")
	public String editVacation(@PathVariable("id") int id, @PathVariable("vacationId") int vacationId, Model model) {
		model.addAttribute("teacher", teacherService.findById(id));
		model.addAttribute("vacation", vacationService.findById(vacationId));
		logger.debug("Show edit vacation page");

		return "teachers/vacations/edit";
	}

	@PatchMapping("/{vacationId}")
	public String update(@ModelAttribute("vacation") Vacation vacation, @PathVariable("id") int id) {
		logger.debug("Update vacation with id {}", id);
		vacation.setTeacher(teacherService.findById(id));
		vacationService.save(vacation);

		return "redirect:/teachers/" + id + "/vacations";
	}

	@DeleteMapping("/{vacationId}")
	public String delete(@PathVariable("id") int id, @PathVariable("vacationId") int vacationId) {
		logger.debug("Delete vacation with id {}", vacationId);
		vacationService.deleteById(vacationId);

		return "redirect:/teachers/" + id + "/vacations";
	}
}
