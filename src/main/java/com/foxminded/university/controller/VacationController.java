package com.foxminded.university.controller;

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

import com.foxminded.university.model.Vacation;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;

@Controller
@RequestMapping("/teachers/{teacherId}/vacations")
public class VacationController {

	private static final Logger logger = LoggerFactory.getLogger(VacationController.class);

	private TeacherService teacherService;
	private VacationService vacationService;

	public VacationController(TeacherService teacherService, VacationService vacationService) {
		this.teacherService = teacherService;
		this.vacationService = vacationService;
	}

	@GetMapping
	public String all(@PathVariable int teacherId, Model model, Pageable pageable) {
		logger.debug("Show index page");
		Page<Vacation> page = vacationService
				.findByTeacherId(pageable, teacherId);
		model.addAttribute("vacations", page);
		model.addAttribute("teacher", teacherService.findById(teacherId));

		return "teachers/vacations/index";
	}

	@GetMapping("/{id}")
	public String showVacation(@PathVariable int id, Model model) {
		logger.debug("Show vacation page with id {}", id);
		model.addAttribute("vacation", vacationService.findById(id));

		return "teachers/vacations/show";
	}

	@GetMapping("/new")
	public String newTeacherVacations(@PathVariable int teacherId, Vacation vacation, Model model) {
		logger.debug("Show create page");
		model.addAttribute("teacher", teacherService.findById(teacherId));

		return "teachers/vacations/new";
	}

	@PostMapping
	public String createVacation(@PathVariable int teacherId, @ModelAttribute Vacation vacation,
			Model model) {
		vacation.setTeacher(teacherService.findById(teacherId));
		vacationService.save(vacation);
		logger.debug("Create new vacation. Id {}", vacation.getId());

		return "redirect:/teachers/" + teacherId + "/vacations";
	}

	@GetMapping("/{id}/edit")
	public String editVacation(@PathVariable int teacherId, @PathVariable int id, Model model) {
		model.addAttribute("teacher", teacherService.findById(teacherId));
		model.addAttribute("vacation", vacationService.findById(id));
		logger.debug("Show edit vacation page");

		return "teachers/vacations/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute Vacation vacation, @PathVariable int teacherId, @PathVariable int id) {
		logger.debug("Update vacation with id {}", id);
		vacation.setTeacher(teacherService.findById(teacherId));
		vacationService.save(vacation);

		return "redirect:/teachers/" + teacherId + "/vacations";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable int teacherId, @PathVariable int id) {
		logger.debug("Delete vacation with id {}", id);
		vacationService.deleteById(id);

		return "redirect:/teachers/" + teacherId + "/vacations";
	}
}
