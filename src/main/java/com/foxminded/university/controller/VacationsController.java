package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Vacation;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;

@Controller
@RequestMapping("/teachers/{id}/vacations")
public class VacationsController {

	@Autowired
	TeacherService teacherService;
	@Autowired
	VacationService vacationService;

	@GetMapping()
	public String showTeacherVacations(@PathVariable("id") int id, Model model) {
		model.addAttribute("teacher", teacherService.findById(id));
		model.addAttribute("vacations", vacationService.findByTeacherId(id));

		return "teachers/vacations/index";
	}
	
	@GetMapping("/{vacationId}")
	public String showVacation(@PathVariable("vacationId") int id, Model model) {
		model.addAttribute("vacation", vacationService.findById(id));

		return "teachers/vacations/show";
	}

	@GetMapping("/new")
	public String newTeacherVacations(@PathVariable("id") int id, Vacation vacation, Model model) {
		model.addAttribute("teacher", teacherService.findById(id));

		return "teachers/vacations/new";
	}

	@PostMapping()
	public String createVacation(@PathVariable("id") int id, @ModelAttribute("vacation") Vacation vacation,
			Model model) {
		vacation.setTeacher(teacherService.findById(id));
		vacationService.save(vacation);

		return "redirect:/teachers/" + id + "/vacations";
	}
}
